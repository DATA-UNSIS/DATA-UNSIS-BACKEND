package com.example.dynamicSQLTest.services.generalServices;

import com.example.dynamicSQLTest.DTOs.request.GeneralQueryRequest;
import com.example.dynamicSQLTest.DTOs.response.QueryResponse;
import com.example.dynamicSQLTest.common.GeneralQuerysConstants;
import com.example.dynamicSQLTest.enums.ETitles;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MajorDistributionService {
    @PersistenceContext
    private EntityManager entityManager;

    public QueryResponse executeMajorDistributionQuery(@NonNull GeneralQueryRequest generalQueryRequest){
        Map<String, Object> parameters = new HashMap<>();
        StringBuilder finalQuery = new StringBuilder(GeneralQuerysConstants.COUNT_MAJOR_DISTRIBUTION);

        if (generalQueryRequest.getSemesters() != null || generalQueryRequest.getSexo() != null){
            parameters = getParameters(generalQueryRequest);
            return executeQueryWithConditions(finalQuery, parameters);
        } else {
            return executeQueryAllOptions(finalQuery);
        }
    }

    private QueryResponse executeQueryAllOptions(StringBuilder query){
        query.append(GeneralQuerysConstants.GROUP_BY_MAJOR_DISTRIBUTION);
        try {
            Query finalQuery = entityManager.createNativeQuery(query.toString());
            @SuppressWarnings("unchecked")
            List<Object[]> results = finalQuery.getResultList();
            if(!results.isEmpty()){
                QueryResponse queryResponse = new QueryResponse();
                queryResponse.setTitle(ETitles.MAJOR_DISTRIBUTION);
                queryResponse.setData(getDataParseDTO(results));
                return queryResponse;
            }else return null;
        } catch (Exception e) {
            System.err.println("Query execution error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Query execution failed: " + e.getMessage(), e);
        }
    }

    private Map<String,Object> getDataParseDTO(List<Object[]> results){
        Map<String,Object> mapResults = new HashMap<>();
        for (Object[] row : results) {
            mapResults.put((String) row[0],row[1]);
        }
        return mapResults;
    }

    private QueryResponse executeQueryWithConditions(StringBuilder query, Map<String, Object> parameters){
        List<String> conditions = new ArrayList<>();
        if (parameters.containsKey("paramSexo")){
            query.append(GeneralQuerysConstants.SEXO_CONDITION );
            if (parameters.containsKey("param1")) query.append(" AND ");
        } else query.append("WHERE ");

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            if (!entry.getKey().equals("paramSexo")) conditions.add(GeneralQuerysConstants.SEMESTER_CONDITION + entry.getKey());
        }

        if (!conditions.isEmpty()) query.append("( ")
                    .append(String.join(" OR ", conditions))
                    .append(" ) ")
                    .append(GeneralQuerysConstants.GROUP_BY_MAJOR_DISTRIBUTION);
        else query.append(GeneralQuerysConstants.GROUP_BY_MAJOR_DISTRIBUTION);

        System.out.println("Query = " + query);
        System.out.println("Conditions = " + conditions);
        try {
            Query finalQuery = entityManager.createNativeQuery(query.toString());

            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                finalQuery.setParameter(entry.getKey(), entry.getValue());
            }

            @SuppressWarnings("unchecked")
            List<Object[]> results = finalQuery.getResultList();

            if(!results.isEmpty()){
                QueryResponse queryResponse = new QueryResponse();
                queryResponse.setTitle(ETitles.MAJOR_DISTRIBUTION);
                queryResponse.setData(getDataParseDTO(results));
                return queryResponse;
            }else return null;

        } catch (Exception e) {
            System.err.println("Query execution error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Query execution failed: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> getParameters(GeneralQueryRequest generalQueryRequest){
        Map<String, Object> parameters = new HashMap<>();
        if (generalQueryRequest.getSexo() != null) parameters.put("paramSexo", generalQueryRequest.getSexo());
        if (generalQueryRequest.getSemesters() != null) {
            List<String> semesterValues = generalQueryRequest.getSemesters();
            int paramCount = semesterValues.size();
            for (String value : semesterValues) {
                parameters.put("param" + paramCount, value);
                paramCount--;
            }
        }
        return parameters;
    }
}