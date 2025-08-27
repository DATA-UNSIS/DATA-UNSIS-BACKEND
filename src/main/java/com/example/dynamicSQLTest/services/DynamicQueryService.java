package com.example.dynamicSQLTest.services;

import com.example.dynamicSQLTest.DTOs.QueryRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DynamicQueryService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Map<String, Object>> executeDynamicQuery(QueryRequest queryRequest) {
        StringBuilder queryBuilder = new StringBuilder(queryRequest.getBaseQuery());
        Map<String, Object> parameters = new HashMap<>();
        Map<String, Object> multiParameters = new HashMap<>();

        // WHERE clause
        if (queryRequest.getFilters() != null && !queryRequest.getFilters().isEmpty()) {
            if (!queryRequest.getBaseQuery().toLowerCase().contains("where")) {
                queryBuilder.append(" WHERE ");
            } else {
                queryBuilder.append(" AND ");
            }

            List<String> conditions = new ArrayList<>();
            int paramCount = 1;
            for (Map.Entry<String, Object> entry : queryRequest.getFilters().entrySet()) {
                if (entry.getValue() != null) {
                    if (!entry.getValue().toString().contains(",")) {
                        String paramName = "param" + paramCount++;
                        conditions.add(entry.getKey() + " = :" + paramName);
                        parameters.put(paramName, entry.getValue());    
                    }else {
                        multiParameters.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            System.out.println(multiParameters.toString());
            queryBuilder.append(String.join(" AND ", conditions));

            //OR 
            if (!multiParameters.isEmpty()) {
                StringBuilder pruebaOr = new StringBuilder();
                if (!conditions.isEmpty()) pruebaOr.append("AND");
                pruebaOr.append(" ( ");
                List<String> multiConditions = new ArrayList<>();
                for (Map.Entry<String, Object> entry : multiParameters.entrySet()) {
                    String condition = entry.getKey();
                    String[] partes = entry.getValue().toString().split(",");
                    for (String parte : partes) {
                        parte = parte.replace("[", "").replace("]", "").trim();
                        String paramName = "param" + paramCount++; 
                        multiConditions.add(condition + " = :" + paramName);
                        parameters.put(paramName,parte);
                    }
                }
                pruebaOr.append(String.join(" OR ", multiConditions)).append(" )");
                queryBuilder.append(pruebaOr);
            }
        }

        // GROUP BY clause
        if (queryRequest.getGroupBy() != null && !queryRequest.getGroupBy().isEmpty()) {
            queryBuilder.append(" GROUP BY ")
                    .append(String.join(", ", queryRequest.getGroupBy()));
        }

        // ORDER BY clause
        if (queryRequest.getOrderBy() != null && !queryRequest.getOrderBy().isEmpty()) {
            queryBuilder.append(" ORDER BY ")
                    .append(String.join(", ", queryRequest.getOrderBy()));
        }

        // LIMIT clause
        if (queryRequest.getLimit() != null && queryRequest.getLimit() > 0) {
            queryBuilder.append(" LIMIT ").append(queryRequest.getLimit());
        }

        String finalQuery = queryBuilder.toString();
        System.out.println("Executing query: " + finalQuery);
        System.out.println("Parameters: " + parameters);

        return executeNativeQuery(finalQuery, parameters);
    }

    private List<Map<String, Object>> executeNativeQuery(String query, Map<String, Object> parameters) {
        try {
            Query nativeQuery = entityManager.createNativeQuery(query);

            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                nativeQuery.setParameter(entry.getKey(), entry.getValue());
            }

            List<Object[]> results = nativeQuery.getResultList();

            if (results.isEmpty()) {
                return Collections.emptyList();
            }

            Object firstResult = results.get(0);
            if (firstResult instanceof Object[]) {
                return transformArrayResults(results);
            } else {
                return transformScalarResults(results, query);
            }

        } catch (Exception e) {
            System.err.println("Query execution error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Query execution failed: " + e.getMessage(), e);
        }
    }

    private List<Map<String, Object>> transformArrayResults(List<Object[]> results) {
        List<Map<String, Object>> transformed = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> rowMap = new LinkedHashMap<>();

            for (int i = 0; i < row.length; i++) {
                rowMap.put("column_" + (i + 1), row[i]);
            }
            transformed.add(rowMap);
        }

        return transformed;
    }

    private List<Map<String, Object>> transformScalarResults(List<Object[]> results, String query) {
        List<Map<String, Object>> transformed = new ArrayList<>();

        String columnName = extractColumnNameFromQuery(query);

        for (Object result : results) {
            Map<String, Object> rowMap = new LinkedHashMap<>();
            rowMap.put(columnName, result);
            transformed.add(rowMap);
        }

        return transformed;
    }

    private String extractColumnNameFromQuery(String query) {
        String lowerQuery = query.toLowerCase();

        if (lowerQuery.contains("count(")) return "count";
        if (lowerQuery.contains("sum(")) return "sum";
        if (lowerQuery.contains("avg(")) return "average";
        if (lowerQuery.contains("min(")) return "minimum";
        if (lowerQuery.contains("max(")) return "maximum";

        if (lowerQuery.contains("select") && lowerQuery.contains("from")) {
            String selectPart = query.substring(query.toLowerCase().indexOf("select") + 6,
                    query.toLowerCase().indexOf("from"));
            String[] parts = selectPart.split(",");
            if (parts.length > 0) {
                String lastPart = parts[parts.length - 1].trim();
                if (lastPart.contains(" as ")) {
                    return lastPart.substring(lastPart.lastIndexOf(" as ") + 4).trim();
                }
                return lastPart;
            }
        }

        return "result";
    }
}