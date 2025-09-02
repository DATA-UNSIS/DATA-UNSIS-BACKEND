package com.example.dynamicSQLTest.services.generalServices;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.dynamicSQLTest.DTOs.request.GeneralQueryRequest;
import com.example.dynamicSQLTest.DTOs.response.QueryResponse;
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
        Query nativeQuery = null;
        String compoundQuery;

        Map<String, Object> dataList = new HashMap<>();
        try{
            //todas las carreras, todos los semestres y todos los sexos (No especifica ninguno == null)
            if(request.getMajors() == null && request.getSemesters() == null && request.getSexo() == null){
                nativeQuery = entityManager.createNativeQuery(GeneralQuerysConstants.COUNT_HOUSE_HOULD_SERVICES);
                Object[] result = (Object[]) nativeQuery.getSingleResult();
                results.setTitle(title);
                for(int i=0; i<result.length; i++){
                    dataList.put(EHouseholdServices.values()[i].toString(), result[i]);
                }
                results.setData(dataList);
                return results;
            }else{
                compoundQuery = getCompoundQuery(request);
                nativeQuery = entityManager.createNativeQuery(compoundQuery);
                
                @SuppressWarnings("unchecked")
                List<Object[]> resultList = nativeQuery.getResultList();
                results.setTitle(title);
                results.setData(getResultsData(request, dataList, resultList, results).getData());
            }
        }catch(Exception e){
            System.err.println("Query execution error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Query execution failed: " + e.getMessage(), e);
        }
        return results;
    }

    private QueryResponse getResultsData(GeneralQueryRequest request, Map<String, Object> dataList, List<Object[]> resultList, QueryResponse results){
                if(request.getMajors() != null && request.getSemesters() != null && request.getSexo() != null){
                    results.setData(queryProcess(resultList, dataList));
                    return results;
                }else
                if(request.getMajors() != null && request.getSemesters() != null && request.getSexo() == null){
                    results.setData(queryProcess(resultList, dataList));
                    return results;
                }else
                if(request.getMajors() != null && request.getSemesters() == null && request.getSexo() != null){
                    results.setData(queryProcess(resultList, dataList));
                    return results;
                }else
                if(request.getMajors() != null && request.getSemesters() == null && request.getSexo() == null){
                    results.setData(queryProcess(resultList, dataList));
                    return results;
                }else
                if(request.getMajors() == null && request.getSemesters() != null && request.getSexo() != null){
                    results.setData(queryProcess(resultList, dataList));
                    return results;
                }else
                if(request.getMajors() == null && request.getSemesters() == null && request.getSexo() != null){
                    results.setData(queryProcess(resultList, dataList));
                    return results;
                }else
                if(request.getMajors() == null && request.getSemesters() != null && request.getSexo() == null){
                    results.setData(queryProcess(resultList, dataList));
                    return results;
                }

                return results;
    }
   //Procesa los resultados de la consulta y los mapea al formato esperado
    private Map<String, Object> queryProcess(List<Object[]> resultList, Map<String, Object> dataList){
        for(Object[] row : resultList) {
                    for(int i=0; i<row.length; i++){
                        dataList.put(EHouseholdServices.values()[i].toString(), row[i]);
                    }
                }
        return dataList;
    }

    private String getCompoundQuery(GeneralQueryRequest request){
        StringBuilder query = new StringBuilder();
        List<String> tables = Arrays.asList("servicios", "alumnos");
        List<String> majors = request.getMajors();
        List<String> semesters = request.getSemesters();
        String sex = request.getSexo();
        

        //Carrera + semestre + sexo (Especifica al menos uno)
        if(request.getMajors() != null && request.getSemesters() != null && request.getSexo() != null){
            query.append("SELECT ").append(GeneralQuerysConstants.FILTERS_COUNT_HOUSE_HOULD_SERVICES);
            query.append(" FROM ").append(String.join(", ", tables));
            query.append(GeneralQuerysConstants.CLAUSULE_WHERE_H_H_S_C);
            String concatenatedMajors = "'" + String.join("', '", majors) + "'";
            query.append(" ("+concatenatedMajors+") ").append(" AND ").append(GeneralQuerysConstants.CLAUSULE_H_H_S_S);
            String concatenatedSemesters = "'" + String.join("', '", semesters) + "'";
            query.append(" ("+concatenatedSemesters+") ").append(" AND ");
            query. append(GeneralQuerysConstants.CLAUSULE_H_H_S_SEX).append(" ('"+sex+"') ").append(";");
            System.out.println("Carrera, semestre, sexo (Especifica al menos uno): "+"\n"+query+"\n\n");
            return query.toString();
        }else
        //carrera + semestre
        if(request.getMajors() != null && request.getSemesters() != null && request.getSexo() == null){
            query.append("SELECT ").append(GeneralQuerysConstants.FILTERS_COUNT_HOUSE_HOULD_SERVICES);
            query.append(" FROM ").append(String.join(", ", tables));
            query.append(GeneralQuerysConstants.CLAUSULE_WHERE_H_H_S_C);
            String concatenatedMajors = "'" + String.join("', '", majors) + "'";
            query.append(" ("+concatenatedMajors+") ").append(" AND ").append(GeneralQuerysConstants.CLAUSULE_H_H_S_S);
            String concatenatedSemesters = "'" + String.join("', '", semesters) + "'";
            query.append(" ("+concatenatedSemesters+") ").append(";");
            System.out.println("carrera + semestre: "+"\n"+query+"\n\n");
            return query.toString();
        }else 
        //carreras + sexo
        if(request.getMajors() != null && request.getSemesters() == null && request.getSexo() != null){
            query.append("SELECT ").append(GeneralQuerysConstants.FILTERS_COUNT_HOUSE_HOULD_SERVICES);
            query.append(" FROM ").append(String.join(", ", tables));
            query.append(GeneralQuerysConstants.CLAUSULE_WHERE_H_H_S_C);
            String concatenatedMajors = "'" + String.join("', '", majors) + "'";
            query.append(" ("+concatenatedMajors+") ").append(" AND ");
            query. append(GeneralQuerysConstants.CLAUSULE_H_H_S_SEX).append(" ('"+sex+"') ").append(";");
            System.out.println("carreras + sexo: "+"\n"+query+"\n\n");
            return query.toString();
        }else
        //carrera
        if(request.getMajors() != null && request.getSemesters() == null && request.getSexo() == null){
            query.append("SELECT ").append(GeneralQuerysConstants.FILTERS_COUNT_HOUSE_HOULD_SERVICES);
            query.append(" FROM ").append(String.join(", ", tables));
            query.append(GeneralQuerysConstants.CLAUSULE_WHERE_H_H_S_C);
            String concatenatedFilters = "'" + String.join("', '", majors) + "'";
            query.append(" ("+concatenatedFilters+") ").append(";");
            System.out.println("carreras: "+"\n"+query+"\n\n");
            return query.toString();
        }else
        //semestres + sexo
        if(request.getMajors() == null && request.getSemesters() != null && request.getSexo() != null){
            query.append("SELECT ").append(GeneralQuerysConstants.FILTERS_COUNT_HOUSE_HOULD_SERVICES);
            query.append(" FROM ").append(String.join(", ", tables)).append(" WHERE ").append(GeneralQuerysConstants.CLAUSULE_H_H_S_S);
            String concatenatedSemesters = "'" + String.join("', '", semesters) + "'";
            query.append(" ("+concatenatedSemesters+") ").append(" AND ");
            query. append(GeneralQuerysConstants.CLAUSULE_H_H_S_SEX).append(" ('"+sex+"') ").append(";");
            System.out.println("semestres + sexo: "+"\n"+query+"\n\n");
            return query.toString();
        }else
        //sexo
        if(request.getMajors() == null && request.getSemesters() == null && request.getSexo() != null){
            query.append("SELECT ").append(GeneralQuerysConstants.FILTERS_COUNT_HOUSE_HOULD_SERVICES);
            query.append(" FROM ").append(String.join(", ", tables)).append(" WHERE ");
            query. append(GeneralQuerysConstants.CLAUSULE_H_H_S_SEX).append(" ('"+sex+"') ").append(";");
            System.out.println("sexo: "+"\n"+query+"\n\n");
            return query.toString();
        }else
        if(request.getMajors() == null && request.getSemesters() != null && request.getSexo() == null){
            query.append("SELECT ").append(GeneralQuerysConstants.FILTERS_COUNT_HOUSE_HOULD_SERVICES);
            query.append(" FROM ").append(String.join(", ", tables)).append(" WHERE ");
            String concatenatedSemesters = "'" + String.join("', '", semesters) + "'";
            query. append(GeneralQuerysConstants.CLAUSULE_H_H_S_S).append(" ("+concatenatedSemesters+") ").append(";");
            System.out.println("semestres: "+"\n"+query+"\n\n");
            return query.toString();
        }
        return query.toString();
    }

}
