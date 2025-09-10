package com.example.dynamicSQLTest.builders;

import com.example.dynamicSQLTest.DTOs.request.GeneralQueryRequest;
import com.example.dynamicSQLTest.common.GeneralQuerysConstants;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HomeworkDevicesQueryBuilder {
    public String buildQuery(GeneralQueryRequest request) {
        StringBuilder query = new StringBuilder();

        query.append("SELECT ");

        query.append(GeneralQuerysConstants.COUNT_HOMEWORK_DEVICES);

        query.append(" FROM alumnos, respuestas, respuestas_seleccion_multiple, opciones");

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
        conditions.add("alumnos.curp = respuestas.curp");
        conditions.add("respuestas.id_respuesta = respuestas_seleccion_multiple.respuesta_id");
        conditions.add("respuestas_seleccion_multiple.opcion_id = opciones.id_opcion");
        conditions.add("respuestas.id_pregunta = 17");

        if (!conditions.isEmpty()) {
            query.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        return query.toString();
    }
}
