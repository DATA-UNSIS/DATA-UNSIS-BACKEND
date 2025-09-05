package com.example.dynamicSQLTest.processors;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.example.dynamicSQLTest.common.Constants.economicDependency;

@Component
public class EconomicDependencyResultProcessor {
    public Map<String, Object> processResults(List<Object[]> resultList) {
        Map<String, Object> data = new LinkedHashMap<>();

        if (resultList.isEmpty()) {
            return createEmptyResult();
        }

        Object[] row = resultList.get(0);

        if (row.length != 7) {
            return createEmptyResult();
        }

        for (int i = 0; i < economicDependency.length && i < row.length; i++) {
            data.put(economicDependency[i], row[i] != null ? row[i] : 0);
        }

        return data;
    }

    private Map<String, Object> createEmptyResult() {
        Map<String, Object> emptyData = new LinkedHashMap<>();
        Map<String, Object> emptyEconomicDependency = new LinkedHashMap<>();

        for (String state : economicDependency) {
            emptyEconomicDependency.put(state, 0);
        }

        emptyData.put("No data found", emptyEconomicDependency);
        return emptyData;
    }
}
