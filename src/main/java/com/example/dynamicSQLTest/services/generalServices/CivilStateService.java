package com.example.dynamicSQLTest.services.generalServices;

import com.example.dynamicSQLTest.DTOs.request.GeneralQueryRequest;
import com.example.dynamicSQLTest.DTOs.response.QueryResponse;
import com.example.dynamicSQLTest.DTOs.utils.DataDTO;
import com.example.dynamicSQLTest.common.GeneralQuerysConstants;
import com.example.dynamicSQLTest.enums.ECivilStates;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CivilStateService {
    @PersistenceContext
    private EntityManager em;

    public QueryResponse executeNativeQuery(GeneralQueryRequest request) {
        QueryResponse queryResponse = new QueryResponse();
        DataDTO dataDto = new DataDTO();
        Query nativeQuery = null;
        String compoundQuery;
        Map<String, Object> dataList = new HashMap<>();

        try {
            if (request.getMajor().getMajors() == null && request.getSemester().getSemesters() == null && request.getSexo() == "") {
                nativeQuery = em.createNativeQuery(GeneralQuerysConstants.COUNT_CIVIL_STATE);
                Object[] result = (Object[]) nativeQuery.getSingleResult();

                List<String> columnNames = getColumnNames();

                Map<String, Object> civilStateCounts = new LinkedHashMap<>();
                for (int i = 0; i < result.length; i++) {
                    String columnName = columnNames.get(i);
                    String civilStateKey = convertColumnNameToCivilState(columnName);
                    civilStateCounts.put(civilStateKey, ((Number) result[i]).longValue());
                }
            } else {

            }

        }
    }

    private List<String> getColumnNames() {
        return Arrays.asList("total_solteros", "total_casados", "total_divorciados",
                "total_union_libre", "total_padre_o_madre_soltero", "total_otro");
    }

    private String convertColumnNameToCivilState(String columnName) {
        switch (columnName) {
            case "total_solteros": return ECivilStates.SOLTERO.toString();
            case "total_casados": return ECivilStates.CASADO.toString();
            case "total_divorciados": return ECivilStates.DIVORCIADO.toString();
            case "total_union_libre": return ECivilStates.UNION_LIBRE.toString();
            case "total_padre_o_madre_soltero": return ECivilStates.PADRE_MADRE_SOLTERO.toString();
            case "total_otro": return ECivilStates.OTRO.toString();
            default: return columnName;
        }
    }
}