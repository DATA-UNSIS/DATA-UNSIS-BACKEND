package com.example.dynamicSQLTest.services.generalServices;

import com.example.dynamicSQLTest.DTOs.request.GeneralQueryRequest;
import com.example.dynamicSQLTest.DTOs.response.QueryResponse;
import com.example.dynamicSQLTest.common.GeneralQuerysConstants;
import com.example.dynamicSQLTest.enums.ETitles;
import io.micrometer.common.lang.NonNull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StateDistributionService {
    @PersistenceContext
    private EntityManager entityManager;

    public QueryResponse executeStateDistributionQuery(@NonNull GeneralQueryRequest request, List<String> tables) {
        String[] tableNames = tables.toArray(new String[0]);
        List<String> majors = request.getMajors();
        List<String> semesters = request.getSemesters();
        String sex = request.getSexo();
        String query = buildFinalQuery(majors, semesters, sex, tableNames);
        return getQueryResponse(query);
    }

    private String buildFinalQuery(List<String> majors, List<String> semesters, String sex, String[] tables) {
        StringBuilder query = new StringBuilder(GeneralQuerysConstants.COUNT_STATE);
        if (majors == null && semesters == null && sex == null)
            query.append("FROM ").append(tables[0]);
        else {
            query.append("FROM ").append(tables[0]).append(", ").append(tables[1]).append(" WHERE ");
            if (majors != null && !majors.isEmpty()) {
                query.append(GeneralQuerysConstants.BACHELORS_DEGREE)
                        .append(" ('")
                        .append(String.join("', '", majors))
                        .append ("') ")
                        .append(" AND ");
            }
            if (semesters != null && !semesters.isEmpty()) {
                query.append(GeneralQuerysConstants.SEMESTER)
                        .append(" ('")
                        .append(String.join("', '", semesters))
                        .append("') ")
                        .append(" AND ");
            }
            if (sex != null && !sex.isEmpty()) {
                query.append(GeneralQuerysConstants.CLAUSULE_SEX)
                        .append(" ( '")
                        .append(sex)
                        .append("') ")
                        .append(" AND ");
            }
            query.append(GeneralQuerysConstants.CURP_CONDITION);
        }
        query.append(GeneralQuerysConstants.GROUP_STATE);
        System.out.println();
        return query.toString();
    }

    private QueryResponse getQueryResponse(String query) {
        try {
            Query finalQuery = entityManager.createNativeQuery(query);
            @SuppressWarnings("unchecked")
            List<Object[]> results = finalQuery.getResultList();
            QueryResponse queryResponse =  new QueryResponse();
            queryResponse.setTitle(ETitles.STATE_DISTRIBUTION);
            Map<String, Object> dataResults = new HashMap<>();

            if(!results.isEmpty()) dataResults = setDataResults(results);
            else dataResults.put("Sin resultados", null);

            queryResponse.setData(dataResults);
            return queryResponse;
        } catch (Exception e) {
            System.err.println("Query execution error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Query execution failed: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> setDataResults(@NonNull List<Object[]> results) {
        Map<String,Object> mapResults = new HashMap<>();
        for (Object[] row : results) {
            mapResults.put((String) row[0],row[1]);
        }
        return mapResults;
    }
}
