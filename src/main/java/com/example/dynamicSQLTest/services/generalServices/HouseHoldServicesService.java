package com.example.dynamicSQLTest.services.generalServices;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.dynamicSQLTest.DTOs.request.GeneralQueryRequest;
import com.example.dynamicSQLTest.DTOs.response.QueryResponse;
import com.example.dynamicSQLTest.DTOs.utils.DataDTO;
import com.example.dynamicSQLTest.common.GeneralQuerysConstants;
import com.example.dynamicSQLTest.enums.EHouseholdServices;
import com.example.dynamicSQLTest.enums.ETitles;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
@Service
public class HouseHoldServicesService {

    @PersistenceContext
    private EntityManager entityManager;

    public QueryResponse executeNativeQuery(ETitles title, GeneralQueryRequest request){
        QueryResponse results = new QueryResponse();
        DataDTO dataDto = new DataDTO(); 
        Query nativeQuery = null;
        String compoundQuery;

        Map<String, Object> dataList = new HashMap<>();
        try{
            if(request.getMajor().getMajors().size()<=0 && request.getSemester().getSemesters().size()<=0 && request.getSexo().isEmpty()){
                System.out.println("Entro a general \n");
                nativeQuery = entityManager.createNativeQuery(GeneralQuerysConstants.COUNT_HOUSE_HOULD_SERVICES);
                Object[] result = (Object[]) nativeQuery.getSingleResult();
                for(int i=0; i<result.length; i++){
                    dataList.put(EHouseholdServices.values()[i].toString(), result[i]);
                }
            }else{
                System.out.println("Entro a compound \n");
                compoundQuery=getCompoundQuery(request);
                nativeQuery = entityManager.createNativeQuery(compoundQuery);
                
                // Para query con filtros (múltiples filas con carrera)
                @SuppressWarnings("unchecked")
                List<Object[]> resultList = nativeQuery.getResultList();
                for(Object[] row : resultList) {
                    String carrera = (String) row[0]; // Primera columna es carrera
                    Map<String, Object> carreraData = new HashMap<>();
                    // Procesar servicios (saltar primera columna que es carrera)
                    for(int i=1; i<row.length; i++){
                        carreraData.put(EHouseholdServices.values()[i-1].toString(), row[i]);
                    }
                    
                    // Agregar datos de esta carrera
                    dataList.put(carrera, carreraData);
                }
            }
            
            results.setTitle(title);
            dataDto.setData(dataList);
            results.setData(dataDto);

        }catch(Exception e){
            System.err.println("Query execution error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Query execution failed: " + e.getMessage(), e);
        }
        return results;
    }

    private String getCompoundQuery(GeneralQueryRequest request){
        StringBuilder query = new StringBuilder();
        List<String> tables = Arrays.asList("servicios", "alumnos");
        List<String> majors = request.getMajor().getMajors();
        List<String> semesters = request.getSemester().getSemesters();

        if(request.getMajor().getMajors().size()>0 && request.getSemester().getSemesters().size()<=0 && request.getSexo().isEmpty()){
            query.append("SELECT ").append(" carrera, "+GeneralQuerysConstants.FILTERS_COUNT_HOUSE_HOULD_SERVICES);
            query.append(" FROM ").append(String.join(", ", tables));
            query.append(GeneralQuerysConstants.CLAUSULE_WHERE_H_H_S_C);
            String concatenatedFilters = "'" + String.join("', '", majors) + "'";
            query.append(" ("+concatenatedFilters+") ").append(" GROUP BY carrera ORDER BY carrera;");
            System.out.println("\n\n\n"+query+"\n\n\n");
        }


        if(request.getMajor().getMajors().size()>0 && request.getSemester().getSemesters().size()>0 && request.getSexo().isEmpty()){
            query.append("SELECT ").append("carrera, semestre, "+GeneralQuerysConstants.FILTERS_COUNT_HOUSE_HOULD_SERVICES);
            query.append(" FROM ").append(String.join(", ", tables));
            query.append(GeneralQuerysConstants.CLAUSULE_WHERE_H_H_S_C);
            String concatenatedMajors = "'" + String.join("', '", majors) + "'";
            query.append(" ("+concatenatedMajors+") ").append(" AND ").append(GeneralQuerysConstants.CLAUSULE_WHERE_H_H_S_S);
            String concatenatedSemesters = "'" + String.join("', '", semesters) + "'";
            query.append(" ("+concatenatedSemesters+") ").append(concatenatedSemesters).append("GROUP BY carrera. semestre ORDER BY carrera. semestre");
        }

        //Falta filtrar eligiendo carreras, no semestres, sexo
        //Falta filtrar eligiendo no carreras, semestres, sexo
        //Falta filtrar eligiendo no carreras, no semestres, sexo
        
        return query.toString();
    }

}
