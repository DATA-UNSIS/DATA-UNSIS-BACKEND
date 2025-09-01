package com.example.dynamicSQLTest.builders;

import com.example.dynamicSQLTest.DTOs.request.GeneralQueryRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CivilStateQueryBuilder {
    public String buildQuery(GeneralQueryRequest request) {
        StringBuilder query = new StringBuilder();

        query.append("SELECT ");

        query.append(getCivilStateCounts());

        query.append(" FROM alumnos");

        List<String> conditions = new ArrayList<>();

        if (request.getMajors() != null && !request.getMajors().isEmpty()) {
            conditions.add("carrera IN (:majors)");
        }
        if (request.getSemesters() != null && !request.getSemesters().isEmpty()) {
            conditions.add("semestre IN (:semesters)");
        }
        if (request.getSexo() != null && !request.getSexo().isEmpty()) {
            conditions.add("sexo = :sexo");
        }

        if (!conditions.isEmpty()) {
            query.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        return query.toString();
    }

    private String getCivilStateCounts() {
        return "count(*) FILTER (WHERE estado_civil = 'Soltero') AS total_solteros, " +
                "count(*) FILTER (WHERE estado_civil = 'Casado') AS total_casados, " +
                "count(*) FILTER (WHERE estado_civil = 'Divorciado') AS total_divorciados, " +
                "count(*) FILTER (WHERE estado_civil = 'Unión Libre') AS total_union_libre, " +
                "count(*) FILTER (WHERE estado_civil = 'Padre/Madre soltero(a)') AS total_padre_o_madre_soltero, " +
                "count(*) FILTER (WHERE estado_civil NOT IN ('Soltero', 'Casado', 'Divorciado', 'Unión Libre', 'Padre/Madre soltero(a)')) AS total_otro";
    }
}
