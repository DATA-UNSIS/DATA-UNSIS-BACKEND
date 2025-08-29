package com.example.dynamicSQLTest.common;

public class GeneralQuerysConstants {
    
    public static final String COUNT_SCHOLARSHIPS_REJECTED = "SELECT " + 
    "COUNT(*) FILTER (WHERE beca_manutencion = true)     AS total_beca_manutencion," + 
    "COUNT(*) FILTER (WHERE conafe = true)               AS total_conafe," + 
    "COUNT(*) FILTER (WHERE jovenes_escribiendo = true)  AS total_jovenes_escribiendo," + 
    "COUNT(*) FILTER (WHERE madre_soltera = true)        AS total_madre_soltera," + 
    "COUNT(*) FILTER (WHERE otra_beca = true)            AS total_otra_beca," + 
    "COUNT(*) FILTER (WHERE semillas_talento = true)     AS total_semillas_talento," + 
    "COUNT(*) FILTER (WHERE otro IS NOT NULL)            AS total_otro" +
    "FROM becas;";

    public static final String COUNT_HOUSE_HOULD_SERVICES = "SELECT "+ 
    "COUNT(*) FILTER (WHERE agua_potable = true)         AS total_agua_potable,"+
    "COUNT(*) FILTER (WHERE drenaje = true)              AS total_drenaje,"+
    "COUNT(*) FILTER (WHERE energia_electrica = true)    AS total_energia_electrica,"+
    "COUNT(*) FILTER (WHERE instalacion_gas = true)      AS total_instalacion_gas,"+
    "COUNT(*) FILTER (WHERE internet = true)             AS total_internet,"+
    "COUNT(*) FILTER (WHERE lavadora_ropa = true)        AS total_lavadora_ropa,"+
    "COUNT(*) FILTER (WHERE microondas = true)           AS total_microondas,"+
	"COUNT(*) FILTER (WHERE refrigerador = true)		 AS total_refrigerador,"+
	"COUNT(*) FILTER (WHERE tel_celular = true)			 AS total_tel_celular,"+
	"COUNT(*) FILTER (WHERE tel_fijo = true)			 AS total_tel_fijo,"+
    "COUNT(*) FILTER (WHERE television = true)			 AS total_television "+
    "FROM servicios;";
    //Todos los filtros
    public static final String FILTERS_COUNT_HOUSE_HOULD_SERVICES = 
    "COUNT(*) FILTER (WHERE agua_potable = true) AS total_agua_potable, "+
    "COUNT(*) FILTER (WHERE drenaje = true) AS total_drenaje, "+
    "COUNT(*) FILTER (WHERE energia_electrica = true) AS total_energia_electrica, "+
    "COUNT(*) FILTER (WHERE instalacion_gas = true) AS total_instalacion_gas, "+
    "COUNT(*) FILTER (WHERE internet = true) AS total_internet, "+
    "COUNT(*) FILTER (WHERE lavadora_ropa = true) AS total_lavadora_ropa, "+
    "COUNT(*) FILTER (WHERE microondas = true) AS total_microondas,"+
	"COUNT(*) FILTER (WHERE refrigerador = true) AS total_refrigerador, "+
	"COUNT(*) FILTER (WHERE tel_celular = true)	AS total_tel_celular, "+
	"COUNT(*) FILTER (WHERE tel_fijo = true) AS total_tel_fijo, "+
    "COUNT(*) FILTER (WHERE television = true) AS total_television ";
    //Filtro por carrera
    public static final String CLAUSULE_WHERE_H_H_S_C = " WHERE carrera IN ";
    //Filtro por semestre
    public static final String CLAUSULE_WHERE_H_H_S_S = " WHERE semestre IN ";
}
