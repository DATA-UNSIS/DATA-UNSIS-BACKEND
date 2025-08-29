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
            //todas las carreras, todos los semestres y todos los sexos (No especifica ninguno)
            if(request.getMajor().getMajors().size()<=0 && request.getSemester().getSemesters().size()<=0 && request.getSexo().isEmpty()){
                System.out.println("Entro a general \n");
                nativeQuery = entityManager.createNativeQuery(GeneralQuerysConstants.COUNT_HOUSE_HOULD_SERVICES);
                System.out.println("Query nativa: \n\n\n"+nativeQuery+"\n\n\n");
                Object[] result = (Object[]) nativeQuery.getSingleResult();
                for(int i=0; i<result.length; i++){
                    dataList.put(EHouseholdServices.values()[i].toString(), result[i]);
                }
            }else{
                System.out.println("Entro a compound \n");
                compoundQuery=getCompoundQuery(request);
                nativeQuery = entityManager.createNativeQuery(compoundQuery);
                
                System.out.println("Esta es la final native query: \n\n\n"+nativeQuery);
                @SuppressWarnings("unchecked")
                List<Object[]> resultList = nativeQuery.getResultList();
                results.setTitle(title);
                results.setData(getResultsData(request, dataDto, dataList, resultList, results).getData());
            }
        }catch(Exception e){
            System.err.println("Query execution error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Query execution failed: " + e.getMessage(), e);
        }
        return results;
    }

    private QueryResponse getResultsData(GeneralQueryRequest request, DataDTO dataDto, Map<String, Object> dataList, List<Object[]> resultList, QueryResponse results){
                if(request.getMajor().getMajors().size()>0 && request.getSemester().getSemesters().size()>0 && !request.getSexo().isEmpty()){
                    dataDto.setData(queryProcessMajorSemesterSex(resultList, dataList));
                    results.setData(dataDto);
                    return results;
                }else
                if(request.getMajor().getMajors().size()>0 && request.getSemester().getSemesters().size()>0 && request.getSexo().isEmpty()){
                    dataDto.setData(queryProcessMajorSemester(resultList, dataList));
                    results.setData(dataDto);
                    return results;
                }else
                if(request.getMajor().getMajors().size()>0 && request.getSemester().getSemesters().size()>0 && !request.getSexo().isEmpty()){
                    dataDto.setData(queryProcessMajorSex(resultList, dataList));
                    results.setData(dataDto);
                    return results;
                }else
                if(request.getMajor().getMajors().size()>0 && request.getSemester().getSemesters().size()<=0 && request.getSexo().isEmpty()){
                    dataDto.setData(queryProcessMajor(resultList, dataList));
                    results.setData(dataDto);
                    return results;
                }else
                if(request.getMajor().getMajors().size()>0 && request.getSemester().getSemesters().size()>0 && !request.getSexo().isEmpty()){
                    dataDto.setData(queryProcessSemesterSex(resultList, dataList));
                    results.setData(dataDto);
                    return results;
                }else
                if(request.getMajor().getMajors().size()>0 && request.getSemester().getSemesters().size()>0 && !request.getSexo().isEmpty()){
                    dataDto.setData(queryProcessSex(resultList, dataList));
                    results.setData(dataDto);
                    return results;
                }   
                return results;
    }

    private Map<String, Object> queryProcessMajorSemesterSex(List<Object[]> resultList, Map<String, Object> dataList){
        return null;
    }

    private Map<String, Object> queryProcessMajorSemester(List<Object[]> resultList, Map<String, Object> dataList){
        return null;
    }

    private Map<String, Object> queryProcessMajorSex(List<Object[]> resultList, Map<String, Object> dataList){
        return null;
    }

    private Map<String, Object> queryProcessSemesterSex(List<Object[]> resultList, Map<String, Object> dataList){
        return null;
    }

    private Map<String, Object> queryProcessSex(List<Object[]> resultList, Map<String, Object> dataList){
        return null;
    }


    private Map<String, Object> queryProcessMajor(List<Object[]> resultList, Map<String, Object> dataList){
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
        return dataList;
    }

    private String getCompoundQuery(GeneralQueryRequest request){
        StringBuilder query = new StringBuilder();
        List<String> tables = Arrays.asList("servicios", "alumnos");
        List<String> majors = request.getMajor().getMajors();
        List<String> semesters = request.getSemester().getSemesters();
        String sex = request.getSexo();

        //Carrera, semestre, sexo (Especifica al menos uno)
        if(request.getMajor().getMajors().size()>0 && request.getSemester().getSemesters().size()>0 && !request.getSexo().isEmpty()){
            query.append("SELECT ").append("carrera, semestre, sexo, "+GeneralQuerysConstants.FILTERS_COUNT_HOUSE_HOULD_SERVICES);
            query.append(" FROM ").append(String.join(", ", tables));
            query.append(GeneralQuerysConstants.CLAUSULE_WHERE_H_H_S_C);
            String concatenatedMajors = "'" + String.join("', '", majors) + "'";
            query.append(" ("+concatenatedMajors+") ").append(" AND ").append(GeneralQuerysConstants.CLAUSULE_H_H_S_S);
            String concatenatedSemesters = "'" + String.join("', '", semesters) + "'";
            query.append(" ("+concatenatedSemesters+") ").append(" AND ");
            query. append(GeneralQuerysConstants.CLAUSULE_H_H_S_SEX).append(" ('"+sex+"') ").append("GROUP BY carrera, semestre, sexo ORDER BY carrera, semestre, sexo;");
            System.out.println("Carrera, semestre, sexo (Especifica al menos uno): "+"\n\n\n"+query+"\n\n\n");
            return query.toString();
        }else
        //carrera, semestre, no sexo esta
        if(request.getMajor().getMajors().size()>0 && request.getSemester().getSemesters().size()>0 && request.getSexo().isEmpty()){
            query.append("SELECT ").append("carrera, semestre, "+GeneralQuerysConstants.FILTERS_COUNT_HOUSE_HOULD_SERVICES);
            query.append(" FROM ").append(String.join(", ", tables));
            query.append(GeneralQuerysConstants.CLAUSULE_WHERE_H_H_S_C);
            String concatenatedMajors = "'" + String.join("', '", majors) + "'";
            query.append(" ("+concatenatedMajors+") ").append(" AND ").append(GeneralQuerysConstants.CLAUSULE_H_H_S_S);
            String concatenatedSemesters = "'" + String.join("', '", semesters) + "'";
            query.append(" ("+concatenatedSemesters+") ").append("GROUP BY carrera, semestre ORDER BY carrera, semestre;");
            System.out.println("carrera, semestre, no sexo: "+"\n\n\n"+query+"\n\n\n");
            return query.toString();
        }else 
        //carreras, no semestres, sexo
        if(request.getMajor().getMajors().size()>0 && request.getSemester().getSemesters().size()>0 && !request.getSexo().isEmpty()){
            query.append("SELECT ").append("carrera, sexo, "+GeneralQuerysConstants.FILTERS_COUNT_HOUSE_HOULD_SERVICES);
            query.append(" FROM ").append(String.join(", ", tables));
            query.append(GeneralQuerysConstants.CLAUSULE_WHERE_H_H_S_C);
            String concatenatedMajors = "'" + String.join("', '", majors) + "'";
            query.append(" ("+concatenatedMajors+") ").append(" AND ");
            query. append(GeneralQuerysConstants.CLAUSULE_H_H_S_SEX).append(" ('"+sex+"') ").append("GROUP BY carrera, sexo ORDER BY carrera, sexo;");
            System.out.println("carreras, no semestres, sexo: "+"\n\n\n"+query+"\n\n\n");
            return query.toString();
        }else
        //carrera, no semestre, no sexo
        if(request.getMajor().getMajors().size()>0 && request.getSemester().getSemesters().size()<=0 && request.getSexo().isEmpty()){
            query.append("SELECT ").append(" carrera, "+GeneralQuerysConstants.FILTERS_COUNT_HOUSE_HOULD_SERVICES);
            query.append(" FROM ").append(String.join(", ", tables));
            query.append(GeneralQuerysConstants.CLAUSULE_WHERE_H_H_S_C);
            String concatenatedFilters = "'" + String.join("', '", majors) + "'";
            query.append(" ("+concatenatedFilters+") ").append(" GROUP BY carrera ORDER BY carrera;");
            System.out.println("carrera, no semestre, no sexo: "+"\n\n\n"+query+"\n\n\n");
            return query.toString();
        }else
        //no carreras, semestres, sexo
        if(request.getMajor().getMajors().size()>0 && request.getSemester().getSemesters().size()>0 && !request.getSexo().isEmpty()){
            query.append("SELECT ").append("semestre, sexo, "+GeneralQuerysConstants.FILTERS_COUNT_HOUSE_HOULD_SERVICES);
            query.append(" FROM ").append(String.join(", ", tables)).append(" WHERE ").append(GeneralQuerysConstants.CLAUSULE_H_H_S_S);
            String concatenatedSemesters = "'" + String.join("', '", semesters) + "'";
            query.append(" ("+concatenatedSemesters+") ").append(" AND ");
            query. append(GeneralQuerysConstants.CLAUSULE_H_H_S_SEX).append(" ('"+sex+"') ").append("GROUP BY semestre, sexo ORDER BY semestre, sexo;");
            System.out.println("no carreras, semestres, sexo: "+"\n\n\n"+query+"\n\n\n");
            return query.toString();
        }else
        //no carreras, no semestres, sexo
        if(request.getMajor().getMajors().size()>0 && request.getSemester().getSemesters().size()>0 && !request.getSexo().isEmpty()){
            query.append("SELECT ").append("sexo, "+GeneralQuerysConstants.FILTERS_COUNT_HOUSE_HOULD_SERVICES);
            query.append(" FROM ").append(String.join(", ", tables)).append(" WHERE ");
            query. append(GeneralQuerysConstants.CLAUSULE_H_H_S_SEX).append(" ('"+sex+"') ").append("GROUP BY sexo ORDER BY sexo;");
            System.out.println("no carreras, no semestres, sexo: "+"\n\n\n"+query+"\n\n\n");
            return query.toString();
        }
        
        return query.toString();
    }

}
