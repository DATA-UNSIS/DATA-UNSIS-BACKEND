package com.example.dynamicSQLTest.services.generalServices;

import com.example.dynamicSQLTest.DTOs.request.GeneralQueryRequest;
import com.example.dynamicSQLTest.DTOs.response.QueryResponse;
import com.example.dynamicSQLTest.DTOs.response.messages.ResponseMessages;
import com.example.dynamicSQLTest.common.Constants;
import com.example.dynamicSQLTest.common.GeneralQuerysConstants;
import com.example.dynamicSQLTest.enums.ETitles;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransportMediumService {
    @Autowired
    private EntityManager entityManager;

    public QueryResponse executeTransportMediumQuery(@NonNull GeneralQueryRequest request, List<String> tables) {
        String[] tableNames = tables.toArray(new String[0]);
        List<String> majors = request.getMajors();
        List<String> semesters = request.getSemesters();
        String sex = request.getSexo();
        String query = buildQuery(majors,semesters,sex,tableNames);
        return getQueryResponse(query);
    }

    private String buildQuery(List<String> majors, List<String> semesters, String sexo, String[] tables) {
        StringBuilder query = new StringBuilder(GeneralQuerysConstants.COUNT_TRANSPORT_MEDIUM). append(" FROM " );
        if ((majors == null || !majors.isEmpty()) && (semesters == null || !semesters.isEmpty()) && (sexo == null || !sexo.isEmpty())){
            query.append(tables[0]);
            return query.toString();
        } else {
            query.append(String.join(" JOIN ", tables));
            String joinClause = String.format(GeneralQuerysConstants.JOIN_ON_CURP, tables[0], tables[1]);
            query.append(joinClause).append(" WHERE ");
            if (majors != null && !majors.isEmpty())
                query.append(GeneralQuerysConstants.BACHELORS_DEGREE)
                        .append(" ('")
                        .append(String.join("', '", majors))
                        .append("')");
            if (majors != null && !majors.isEmpty() && semesters != null && !semesters.isEmpty()) query.append(" AND ");
            if (semesters != null && !semesters.isEmpty())
                query.append(GeneralQuerysConstants.SEMESTER)
                        .append(" ('")
                        .append(String.join("', '", semesters))
                        .append("')");
            if (((majors != null && !majors.isEmpty()) || (semesters != null && !semesters.isEmpty())) && (sexo != null && !sexo.isEmpty())) query.append(" AND ");
            if (sexo != null && !sexo.isEmpty())
                query.append(GeneralQuerysConstants.CLAUSULE_SEX).append(" ('").append(sexo).append("')");
            String finalQuery = query.toString();
            finalQuery = finalQuery.replaceAll("''", "'");
            return finalQuery;
        }
    }

    private QueryResponse getQueryResponse(String query) {
        try {
            Query finalQuery = entityManager.createNativeQuery(query);
            @SuppressWarnings("unchecked")
            List<Object[]> results = finalQuery.getResultList();
            QueryResponse queryResponse =  new QueryResponse();
            queryResponse.setTitle(ETitles.TRANSPORTATION_MEDIUM);
            Map<String, Object> dataResults = new HashMap<>();

            if(!results.isEmpty()) queryResponse.setData(setDataResults(results));
            else {
                dataResults.put("data", ResponseMessages.DATA_NOT_FOUND);
                queryResponse.setData(dataResults);
            }

            return queryResponse;
        } catch (Exception e) {
            System.err.println("Query execution error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Query execution failed: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> setDataResults(List<Object[]> results) {
        Map<String, Object> dataResults = new HashMap<>();
        Object[] result = results.getFirst();
        for (int i = 0; i < result.length; i++) {
            dataResults.put(Constants.transportsMedium[i], result[i]);
        }
        return dataResults;
    }

}
