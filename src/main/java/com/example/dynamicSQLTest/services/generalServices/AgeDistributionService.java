package com.example.dynamicSQLTest.services.generalServices;

import com.example.dynamicSQLTest.DTOs.request.GeneralQueryRequest;
import com.example.dynamicSQLTest.DTOs.response.QueryResponse;
import com.example.dynamicSQLTest.DTOs.response.messages.ResponseMessages;
import com.example.dynamicSQLTest.common.GeneralQuerysConstants;
import com.example.dynamicSQLTest.enums.ETitles;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AgeDistributionService {
    @Autowired
    private EntityManager entityManager;

    public QueryResponse executeAgeDistributionQuery(GeneralQueryRequest request) {
        List<String> majors = request.getMajors();
        List<String> semesters = request.getSemesters();
        String sexo = request.getSexo();
        String finalQuery = buildQuery(majors, semesters, sexo);
        return getQueryResponse(finalQuery);
    }

    private String buildQuery(List<String> majors, List<String> semesters, String sexo) {
        StringBuilder query = new StringBuilder(GeneralQuerysConstants.SELECT_CURP_QUERY);
        if ((majors == null || majors.isEmpty()) && (semesters == null || semesters.isEmpty()) && (sexo == null || sexo.isEmpty()))
            return query.toString();
        else {
            query.append(" WHERE ");
            if (majors != null && !majors.isEmpty()) {
                query.append(GeneralQuerysConstants.BACHELORS_DEGREE)
                        .append("('")
                        .append(String.join("','", majors))
                        .append("') ");
            }
            if ((majors != null && !majors.isEmpty()) && (semesters != null && !semesters.isEmpty())) query.append(" AND ");
            if (semesters != null && !semesters.isEmpty()) {
                query.append(GeneralQuerysConstants.SEMESTER)
                        .append("('")
                        .append(String.join("','", semesters))
                        .append("') ");
            }
            if (majors != null && semesters != null && sexo != null) query.append(" AND ");
            if (sexo != null && !sexo.isEmpty())
                query.append(GeneralQuerysConstants.CLAUSULE_SEX).append("('").append(sexo).append("') ");
            String finalQuery = query.toString();
            finalQuery = finalQuery.replaceAll("''", "'");
            return finalQuery;
        }
    }

    private QueryResponse getQueryResponse(String query) {
        try {
            Query finalQuery = entityManager.createNativeQuery(query);
            @SuppressWarnings("unchecked")
            List<String> results = finalQuery.getResultList();
            QueryResponse queryResponse =  new QueryResponse();
            queryResponse.setTitle(ETitles.AGE_DISTRIBUTION);
            queryResponse.setData(getDataResults(results));
            return queryResponse;
        } catch (Exception e) {
            System.err.println("Query execution error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Query execution failed: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> getDataResults(List<String> results) {
        Map<String, Object> data = new HashMap<>();
        if (results == null || results.isEmpty()) data.put("Message", ResponseMessages.DATA_NOT_FOUND);
        else {
            for (String result : results) {
                result = result.substring(4, 10);
                int year = getAge(result);
                if (year == 0 ) continue;
                String key = Integer.toString(year);
                if (!data.containsKey(key)) {
                    data.put(key, 1);
                } else {
                    int count = (int) data.get(key);
                    data.replace(key, count + 1);
                }
            }
        }
        return data;
    }

    private int getAge(String date) {
        if (date.equals("000000")) return 0;
        //Obtener fecha
        int year = Integer.parseInt(date.substring(0,2));
        int month = Integer.parseInt(date.substring(2,4));
        int day = Integer.parseInt(date.substring(4));

        LocalDate actualDate = LocalDate.now();
        int actualYear = actualDate.getYear();
        int actualShortYear = actualYear % 100;
        int diference = year - actualShortYear;

        int completeYear;

        if (diference <= 50 && diference >= -50) {
            int sigloActual = actualYear / 100;
            int base = sigloActual * 100;
            completeYear = base + year;
        } else if (diference > 50) {
            int sigloAnterior = (actualYear / 100) -1;
            int base = sigloAnterior * 100;
            completeYear = base + year;
        } else {
            int sigloSiguiente =  (actualYear / 100) +1;
            int base = sigloSiguiente * 100;
            completeYear = base + year;
        }

        LocalDate finalDate = LocalDate.of(completeYear, month, day);

        return Period.between(finalDate, actualDate).getYears();
    }
}
