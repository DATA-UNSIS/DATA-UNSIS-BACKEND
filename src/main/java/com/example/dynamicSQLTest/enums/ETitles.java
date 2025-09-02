package com.example.dynamicSQLTest.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ETitles {
    MAJOR_DISTRIBUTION (1, "Cuenta cuantos alumnos hay en cada licenciatura, en esta gráfica siempre seran todas las licenciaturas (El usuario no puede elegir licenciaturas especificas), tiene que verificar que semestres se le manda y si son ambos sexos o solo uno"),
    ECONOMIC_LEVEL (2, "Se mostrarán rangos de ingresos por ejemplo: personas que tienen ingresos menores que 1000, entre 1000 y 2000 y asi, en este si se van a poder mandar mas de una licenciatura y mas de un semestre"),
    SCHOLARSHIPS_REQUESTED (3, "Se mostrará cuantas personas solicitaron cada una de las becas posibles, checar los nombre de cada una de las becas posibles para realizar las consultas."),
    HOUSEHOLD_SERVICES (4, "Se mostrará cuantas personas tienen cada servicio con los filtros aplicados. Ejemplo: TV 10, microondas 20, etc.."),
    CIVIL_STATE (5, "Se mostrará cuantas personas hay con cada uno de los estados civiles con los filtros aplicados. ejemplo: soltero 30, casado 20, etc..."),
    SEX_DISTRIBUTION (6, "Graficos que muestra la distribucion de alumnos por sexo"),
    AGE_DISTRIBUTION (7, "Grafico que muestra la distribucion de alumnos por edad"),
    SEMESTER_DISTRIBUTION (8, "Grafico que muestra la distribucion de alumnos por semestre"),
    STATE_DISTRIBUTION (9, "Grafico que muestra la distribucion de alumnos por estado"),
    MUNICIPALITY_DISTRIBUTION (10, "Grafico que muestra la distribucion de alumnos por municipio"),
    TYPE_INSTITUTION_PROCEDENCY (11, "Grafico que muestra la distribucion de alumnos por tipo de institucion de procedencia"),
    FAMILY_HOUSE (12, "Grafico que muestra la distribucion de alumnos por tipo de vivienda familiar (viven en casa propia, rentada, prestada o viaja diariamente)"),
    TRANSPORTATION_MEDIUM (13, "Grafico que muestra el medio de transporte utilizado por los alumnos"),
    HOMEWORK_DEVICES (14, "Grafico que muestra los medios de apoyo para estudiar en casa");

    private final int id;
    private final String description;
}
