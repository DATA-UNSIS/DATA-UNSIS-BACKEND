package com.example.dynamicSQLTest.builders;

import com.example.dynamicSQLTest.DTOs.request.GeneralQueryRequest;
import com.example.dynamicSQLTest.common.GeneralQuerysConstants;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InstitutionOriginQueryBuilder {
    public String buildQuery(GeneralQueryRequest request){
        StringBuilder query = new StringBuilder();

        query.append("SELECT ");

        query.append(GeneralQuerysConstants.COUNT_INSTITUTION_ORIGIN);

        query.append(" FROM formacion_academica, alumnos");

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
        conditions.add("formacion_academica.curp = alumnos.curp");

        if (!conditions.isEmpty()) {
            query.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        return query.toString();
    }
}
