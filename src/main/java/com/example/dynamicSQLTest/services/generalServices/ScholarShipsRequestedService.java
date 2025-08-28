package com.example.dynamicSQLTest.services.generalServices;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.dynamicSQLTest.enums.EScholarships;
import com.example.dynamicSQLTest.enums.ETitles;
import com.example.dynamicSQLTest.DTOs.response.QueryResponse;
import com.example.dynamicSQLTest.DTOs.utils.DataDTO;
import com.example.dynamicSQLTest.common.GeneralQuerysConstants;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class ScholarShipsRequestedService {

    @PersistenceContext
    private EntityManager entityManager;
    //Se consultan cuantas personas solicitaron cada una de las becas posibles
    public QueryResponse executeNativeQuery(ETitles title) {
        QueryResponse results = new QueryResponse();
        DataDTO dataDto = new DataDTO(); 
        Map<String, Object> dataList = new HashMap<>();
        try {
            Query nativeQuery = entityManager.createNativeQuery(GeneralQuerysConstants.COUNT_SCHOLARSHIPS_REJECTED);
            
            // Ejecutar la consulta y obtener los resultados
            Object[] result = (Object[]) nativeQuery.getSingleResult();
            for(int i=0; i<result.length;i++){
                dataList.put(EScholarships.values()[i].toString(), result[i]); 
            }
            results.setTitle(title);
            dataDto.setData(dataList);
            results.setData(dataDto);  
            return results;

        } catch (Exception e) {
            System.err.println("Query execution error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Query execution failed: " + e.getMessage(), e);
        }
    }

}
