package com.example.dynamicSQLTest.services.generalServices;

import com.example.dynamicSQLTest.DTOs.request.GeneralQueryRequest;
import com.example.dynamicSQLTest.DTOs.response.QueryResponse;
import com.example.dynamicSQLTest.common.Constants;
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
public class EconomicLevelService {
    @PersistenceContext
    private EntityManager entityManager;

    public QueryResponse executeEconomicLevelQuery(@NonNull GeneralQueryRequest request){
        StringBuilder query = new StringBuilder(GeneralQuerysConstants.CHOSE_STUDENTS);
        if (request.getMajors() != null || request.getSemesters() != null){
            List<String> majors = request.getMajors();
            List<String> semesters = request.getSemesters();
            return executeQueryWithConditions(query, majors, semesters);
        } else {
            return executeQueryWithoutConditions(query);
        }
    }

    private QueryResponse executeQueryWithoutConditions(StringBuilder query){
        query.append("),").append(GeneralQuerysConstants.SUM_EARNINGS).append(GeneralQuerysConstants.COUNT_ECONOMIC_LEVEL);
        System.out.println("Query = " + query);
        return getQueryResponse(query);
    }

    private QueryResponse getQueryResponse(StringBuilder query) {
        try {
            Query finalQuery = entityManager.createNativeQuery(query.toString());
            @SuppressWarnings("unchecked")
            List<Object[]> results = finalQuery.getResultList();
            if(!results.isEmpty()){
                QueryResponse queryResponse = new QueryResponse();
                queryResponse.setTitle(ETitles.ECONOMIC_LEVEL);
                Map<String, Object> dataResults = setDataResults(results);
                queryResponse.setData(dataResults);
                return queryResponse;
            }else return null;
        } catch (Exception e) {
            System.err.println("Query execution error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Query execution failed: " + e.getMessage(), e);
        }
    }

    private QueryResponse executeQueryWithConditions(StringBuilder query, List<String> majors, List<String> semesters){
        query.append(" WHERE ");
        if (majors != null) query.append(GeneralQuerysConstants.BACHELORS_DEGREE)
                .append("( '")
                .append(String.join("','", majors))
                .append("' ) ");
        if (semesters != null && majors != null) query.append(" AND ");
        if (semesters != null)  query.append(GeneralQuerysConstants.SEMESTER)
                .append("( '")
                .append(String.join("','", semesters))
                .append("') ");
        query.append(" ), ").append(GeneralQuerysConstants.SUM_EARNINGS).append(GeneralQuerysConstants.COUNT_ECONOMIC_LEVEL);
        System.out.println("Query = " + query);
        return getQueryResponse(query);
    }

    //Metodo para mapear los resultados a clase economica correspondiente
    private Map<String, Object> setDataResults(List<Object[]> results){
        Map<String, Object> dataResults = new HashMap<>();
        Object[] result = results.getFirst();
        for (int i = 0; i < result.length; i++) {
            dataResults.put(Constants.CLASES[i], result[i] );
        }
        return dataResults;
    }

}
