package com.example.dynamicSQLTest.processors;

import com.example.dynamicSQLTest.DTOs.request.GeneralQueryRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class CivilStateResultProcessor {

    public Map<String, Object> processResults(List<Object[]> resultList, GeneralQueryRequest request) {
        Map<String, Object> data = new LinkedHashMap<>();

        if (resultList.isEmpty()) {
            return createEmptyResult();
        }

        Object[] row = resultList.get(0);

        if (row.length != 6) {
            return createEmptyResult();
        }

        String[] civilStates = {
                "total_solteros", "total_casados", "total_divorciados",
                "total_union_libre", "total_padre_o_madre_soltero", "total_otro"
        };

        for (int i = 0; i < civilStates.length && i < row.length; i++) {
            data.put(civilStates[i], row[i] != null ? row[i] : 0);
        }

        return data;
    }

    private Map<String, Object> createEmptyResult() {
        Map<String, Object> emptyData = new LinkedHashMap<>();
        Map<String, Object> emptyCivilState = new LinkedHashMap<>();

        String[] civilStates = {
                "total_solteros", "total_casados", "total_divorciados",
                "total_union_libre", "total_padre_o_madre_soltero", "total_otro"
        };

        for (String state : civilStates) {
            emptyCivilState.put(state, 0);
        }

        emptyData.put("No data found", emptyCivilState);
        return emptyData;
    }
}