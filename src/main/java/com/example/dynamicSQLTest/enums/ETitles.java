package com.example.dynamicSQLTest.enums;

import lombok.Getter;

@Getter
public enum ETitles {
    MAJOR_DISTRIBUTION (1, "Cuenta cuantos alumnos hay en cada licenciatura, en esta gráfica siempre seran todas las licenciaturas (El usuario no puede elegir licenciaturas especificas), tiene que verificar que semestres se le manda y si son ambos sexos o solo uno"),
    ECONOMIC_LEVEL (2, "Se mostrarán rangos de ingresos por ejemplo: personas que tienen ingresos menores que 1000, entre 1000 y 2000 y asi, en este si se van a poder mandar mas de una licenciatura y mas de un semestre"),
    SCHOLARSHIPS_REQUESTED (3, "Se mostrará cuantas personas solicitaron cada una de las becas posibles, checar los nombre de cada una de las becas posibles para realizar las consultas."),
    HOUSEHOLD_SERVICES (4, "Se mostrará cuantas personas tienen cada servicio con los filtros aplicados. Ejemplo: TV 10, microondas 20, etc.."),
    CIVIL_STATE (5, "Se mostrará cuantas personas hay con cada uno de los estados civiles con los filtros aplicados. ejemplo: soltero 30, casado 20, etc...");

    private final int id;
    private final String description;

    ETitles(int id, String description) {
        this.id = id;
        this.description = description;
    }
}
