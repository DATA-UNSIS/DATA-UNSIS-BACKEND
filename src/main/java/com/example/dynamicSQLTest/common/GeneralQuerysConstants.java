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
}
