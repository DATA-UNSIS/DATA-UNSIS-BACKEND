package com.example.dynamicSQLTest.common;

public class GeneralQuerysConstants {
    public static final String COUNT_SCHOLARSHIPS_REJECTED = "SELECT " +
            "       COUNT(*) FILTER (WHERE beca_manutencion = true)     AS total_beca_manutencion," +
            "       COUNT(*) FILTER (WHERE conafe = true)               AS total_conafe," +
            "       COUNT(*) FILTER (WHERE jovenes_escribiendo = true)  AS total_jovenes_escribiendo," +
            "       COUNT(*) FILTER (WHERE madre_soltera = true)        AS total_madre_soltera," +
            "       COUNT(*) FILTER (WHERE otra_beca = true)            AS total_otra_beca," +
            "       COUNT(*) FILTER (WHERE semillas_talento = true)     AS total_semillas_talento," +
            "       COUNT(*) FILTER (WHERE otro IS NOT NULL)            AS total_otro" +
            "       FROM becas;";

    public static final String COUNT_CIVIL_STATE = "SELECT " +
            "count(*) FILTER (WHERE estado_civil = 'Soltero') AS total_solteros, " +
            "count(*) FILTER (WHERE estado_civil = 'Casado') AS total_casados, " +
            "count(*) FILTER (WHERE estado_civil = 'Divorciado') AS total_divorciados, " +
            "count(*) FILTER (WHERE estado_civil = 'Unión Libre') AS total_union_libre, " +
            "count(*) FILTER (WHERE estado_civil = 'Padre/Madre soltero(a)') AS total_padre_o_madre_soltero, " +
            "count(*) FILTER (WHERE estado_civil NOT IN ('Soltero', 'Casado', 'Divorciado', 'Unión Libre', 'Padre/Madre soltero(a)')) AS total_otro " +
            "FROM alumnos";
}