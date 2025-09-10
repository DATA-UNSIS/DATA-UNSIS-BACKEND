package com.example.dynamicSQLTest.services.generalServices;

import com.example.dynamicSQLTest.DTOs.request.GeneralQueryRequest;
import com.example.dynamicSQLTest.DTOs.response.QueryResponse;
import com.example.dynamicSQLTest.builders.EconomicDependencyQueryBuilder;
import com.example.dynamicSQLTest.enums.ETitles;
import com.example.dynamicSQLTest.processors.EconomicDependencyResultProcessor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EconomicDependencyService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private EconomicDependencyQueryBuilder queryBuilder;

    @Autowired
    private EconomicDependencyResultProcessor resultProcessor;

    public QueryResponse executeNativeQuery(ETitles title, GeneralQueryRequest request) {
        QueryResponse response = new QueryResponse();
        response.setTitle(title);

        try {
            String queryString = queryBuilder.buildQuery(request);

            Query query = entityManager.createNativeQuery(queryString);
            setQueryParameters(query, request);

            @SuppressWarnings("unchecked")
            List<Object[]> resultList = query.getResultList();

            response.setData(resultProcessor.processResults(resultList));

        } catch (Exception e) {
            throw new RuntimeException("Query execution failed: " + e.getMessage(), e);
        }

        return response;
    }

    private void setQueryParameters(Query query, GeneralQueryRequest request) {
        if (request.getMajors() != null && !request.getMajors().isEmpty()) {
            query.setParameter("majors", request.getMajors());
        }
        if (request.getSemesters() != null && !request.getSemesters().isEmpty()) {
            query.setParameter("semesters", request.getSemesters());
        }
        if (request.getSexo() != null && !request.getSexo().isEmpty()) {
            query.setParameter("sexo", request.getSexo());
        }
    }
}
