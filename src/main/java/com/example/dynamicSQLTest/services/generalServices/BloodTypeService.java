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
public class BloodTypeService {
    @Autowired
    public EntityManager entityManager;

    public QueryResponse executeBloodTypeQuery(@NonNull GeneralQueryRequest request, String tableName) {
        List<String> majors =  request.getMajors();
        List<String> semesters = request.getSemesters();
        String sex = request.getSexo();
        String query = buildQuery(majors, semesters, sex, tableName);
        return getQueryResponse(query);
    }

    private String buildQuery(List<String> majors, List<String> semesters, String sex, String tableName) {
        StringBuilder query = new StringBuilder(GeneralQuerysConstants.COUNT_BLOOD_TYPE)
                .append(" FROM ").append(tableName);
        if ((majors == null || majors.isEmpty()) && (semesters == null || semesters.isEmpty()) && (sex == null || sex.isEmpty()))
            return query.toString();
        else {
            query.append(" WHERE ");
            if (majors != null && !majors.isEmpty())
                query.append(GeneralQuerysConstants.BACHELORS_DEGREE)
                        .append(" ('")
                        .append(String.join("','", majors))
                        .append("')");
            if (majors != null && !majors.isEmpty() && semesters != null && !semesters.isEmpty()) query.append(" AND ");
            if (semesters != null && !semesters.isEmpty())
                query.append(GeneralQuerysConstants.SEMESTER)
                        .append(" ('")
                        .append(String.join("','", semesters))
                        .append("')");
            if (((majors != null && !majors.isEmpty()) || (semesters != null && !semesters.isEmpty())) && (sex != null && !sex.isEmpty())) query.append(" AND ");
            if (sex != null && !sex.isEmpty())
                query.append(GeneralQuerysConstants.CLAUSULE_SEX)
                        .append(" ('").append(sex).append("')");

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
            queryResponse.setTitle(ETitles.BLOOD_TYPE);
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
        Object[] row = results.getFirst();
        for (int i = 0; i < row.length; i++) {
            dataResults.put(Constants.bloodType[i], row[i] );
        }
        return dataResults;
    }
}
