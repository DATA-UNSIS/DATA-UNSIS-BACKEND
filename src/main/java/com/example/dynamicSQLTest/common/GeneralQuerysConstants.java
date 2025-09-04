package com.example.dynamicSQLTest.common;

public class GeneralQuerysConstants {
    
    public static final String COUNT_SCHOLARSHIPS_REJECTED = "SELECT " + 
    "COUNT(*) FILTER (WHERE beca_manutencion = true)     AS total_beca_manutencion," + 
    "COUNT(*) FILTER (WHERE conafe = true)               AS total_conafe," + 
    "COUNT(*) FILTER (WHERE jovenes_escribiendo = true)  AS total_jovenes_escribiendo," + 
    "COUNT(*) FILTER (WHERE madre_soltera = true)        AS total_madre_soltera," + 
    "COUNT(*) FILTER (WHERE otra_beca = true)            AS total_otra_beca," + 
    "COUNT(*) FILTER (WHERE semillas_talento = true)     AS total_semillas_talento," + 
    "COUNT(*) FILTER (WHERE otro IS NOT NULL)            AS total_otro " +
    "FROM becas;";
    public static final String FILTERS_COUNT_SCHOLARSHIPS_REJECTED =
            "COUNT(*) FILTER (WHERE beca_manutencion = true)     AS total_beca_manutencion, " +
            "COUNT(*) FILTER (WHERE conafe = true)               AS total_conafe, " +
            "COUNT(*) FILTER (WHERE jovenes_escribiendo = true)  AS total_jovenes_escribiendo, " +
            "COUNT(*) FILTER (WHERE madre_soltera = true)        AS total_madre_soltera, " +
            "COUNT(*) FILTER (WHERE otra_beca = true)            AS total_otra_beca, " +
            "COUNT(*) FILTER (WHERE semillas_talento = true)     AS total_semillas_talento, " +
            "COUNT(*) FILTER (WHERE otro IS NOT NULL)            AS total_otro ";

    public static final String COUNT_MAJOR_DISTRIBUTION = "SELECT carrera, COUNT(*) AS value FROM alumnos ";
    public static final String GROUP_BY_MAJOR_DISTRIBUTION = "GROUP BY carrera";
    public static final String SEXO_CONDITION = "WHERE sexo =:paramSexo ";
    public static final String SEMESTER_CONDITION = "semestre =:";

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
    public static final String CLAUSULE_WHERE_MAJORS = " WHERE carrera IN ";
    //Filtro por semestre
    public static final String CLAUSULE_SEMESTERS = " semestre IN ";
    //Filtro por sexo
    public static final String CLAUSULE_SEX = " sexo IN ";

    //Filtros ingresos
    public static final String COUNT_ECONOMIC_LEVEL = "SELECT " +
            "COUNT(*) FILTER ( WHERE i >= 0 AND i <= 2699 ) AS Pobreza_Extrema, " +
            "COUNT(*) FILTER ( WHERE i >= 2700 AND i <= 6799 ) AS Clase_Pobre, " +
            "COUNT(*) FILTER ( WHERE i >= 6800 AND i <= 11599 ) AS Clase_Media_Baja, " +
            "COUNT(*) FILTER ( WHERE i >= 11600 AND i <= 34999 ) AS Clase_Media, " +
            "COUNT(*) FILTER ( WHERE i >= 35000 AND i <= 84999 ) AS Clase_Media_Alta, " +
            "COUNT(*) FILTER ( WHERE i >= 85500 ) AS Clase_Alta " +
            "FROM ingresos_agrupados ig " +
            "INNER JOIN alumnos_elegidos ae ON ig.curp = ae.curp";
    public static final String CHOSE_STUDENTS = "WITH alumnos_elegidos AS ( SELECT curp FROM alumnos ";
    public static final String SUM_EARNINGS = "ingresos_agrupados AS (SELECT curp, SUM(ingreso) AS i FROM ingresos GROUP BY curp ) ";
    public static final String SEMESTER = " semestre IN ";
    public static final String BACHELORS_DEGREE = " carrera IN ";
  
    public static final String COUNT_CIVIL_STATE = "count(*) FILTER (WHERE estado_civil = 'Soltero') AS total_solteros, " +
            "count(*) FILTER (WHERE estado_civil = 'Casado') AS total_casados, " +
            "count(*) FILTER (WHERE estado_civil = 'Divorciado') AS total_divorciados, " +
            "count(*) FILTER (WHERE estado_civil = 'Unión Libre') AS total_union_libre, " +
            "count(*) FILTER (WHERE estado_civil = 'Padre/Madre soltero(a)') AS total_padre_o_madre_soltero, " +
            "count(*) FILTER (WHERE estado_civil NOT IN ('Soltero', 'Casado', 'Divorciado', 'Unión Libre', 'Padre/Madre soltero(a)')) AS total_otro";

    public static final String JOIN_ON_CURP = " ON %s.curp = %s.curp ";

    public static final String COUNT_MUNICIPALITY_DISTRIBUTION = "SELECT municipio, COUNT(*) AS total from alumnos join lugar_procedencia on alumnos.curp=lugar_procedencia.curp GROUP BY municipio; ";
    public static final String FILTERS_COUNT_MUNICIPALITY_DISTRIBUTION = " municipio, COUNT(*) AS total ";

    public static final String COUNT_INSTITUTION_ORIGIN = "count(*) FILTER (WHERE sub_educativo = 'IEBO') AS total_IEBO, " +
            "count(*) FILTER (WHERE sub_educativo = 'COBAO') AS total_COBAO, " +
            "count(*) FILTER (WHERE sub_educativo = 'CBTIS') AS total_CBTIS, " +
            "count(*) FILTER (WHERE sub_educativo = 'CBTA') AS total_CBTA, " +
            "count(*) FILTER (WHERE sub_educativo = 'CETIS') AS total_CETIS, " +
            "count(*) FILTER (WHERE sub_educativo = 'CECYTE PLANTEL') AS total_CECYTE_PLANTEL, " +
            "count(*) FILTER (WHERE sub_educativo = 'CECYTE EMSAD') AS total_CECYTE_EMSAD, " +
            "count(*) FILTER (WHERE sub_educativo = 'CONALEP') AS total_CONALEP, " +
            "count(*) FILTER (WHERE sub_educativo = 'BACHILLERATO INTEGRAL COMUNITARIO (BIC)') AS total_BIC, " +
            "count(*) FILTER (WHERE sub_educativo = 'PREPARATORIA UABJO') AS total_PREPARATORIO_UABJO, " +
            "count(*) FILTER (WHERE sub_educativo = 'TELEBACHILLERATO (TBC)') AS total_TBC, " +
            "count(*) FILTER (WHERE sub_educativo = 'CBTF') AS total_CBTF, " +
            "count(*) FILTER (WHERE sub_educativo = 'CETMAR') AS total_CETMAR, " +
            "count(*) FILTER (WHERE sub_educativo = 'CDART Miguel Cabrera') AS total_CDART, " +
            "count(*) FILTER (WHERE sub_educativo = 'PREFECO') AS total_PREFECO, " +
            "count(*) FILTER (WHERE sub_educativo = 'PREPARATORIA ABIERTA') AS total_PREPARATORIA_ABIERTA, " +
            "count(*) FILTER (WHERE sub_educativo = 'BACHILLERATO PARTICULAR') AS total_BACHILLERATO_PARTICULAR, " +
            "count(*) FILTER (WHERE sub_educativo = 'CEB') AS total_CEB, " +
            "count(*) FILTER (WHERE sub_educativo NOT IN ('COBAO','CBTIS','CBTA','CETIS','CECYTE PLANTEL','CECYTE EMSAD','CONALEP','BACHILLERATO INTEGRAL COMUNITARIO (BIC)','PREPARATORIA UABJO','TELEBACHILLERATO (TBC)','CBTF','CETMAR','CDART Miguel Cabrera','PREFECO','PREPARATORIA ABIERTA','BACHILLERATO PARTICULAR','CEB')) AS total_otro";

        //Filtra por semestre
    public static final String COUNT_SEMESTER_DISTRIBUTION = "SELECT semestre, COUNT(*) AS total FROM alumnos GROUP BY semestre;";
    public static final String FILTERS_COUNT_SEMESTER_DISTRIBUTION = " semestre, COUNT(*) AS total ";

    public static final String COUNT_SEX_DISTRIBUTION = "SELECT sexo, COUNT(*) AS total FROM alumnos GROUP BY sexo;";
    public static final String FILTERS_COUNT_SEX_DISTRIBUTION= " sexo, COUNT(*) AS total ";

    public static final String COUNT_STATE = "SELECT entidad, count(entidad) ";
    public static final String GROUP_STATE = " GROUP BY entidad";
    public static final String CURP_CONDITION = "lugar_procedencia.curp = alumnos.curp";

    public static final String COUNT_FAMILY_HOUSE = "count(*) FILTER (WHERE opcion_id = 31) AS total_rentada, " +
            "count(*) FILTER (WHERE opcion_id = 32) AS total_prestada, " +
            "count(*) FILTER (WHERE opcion_id = 33) AS total_propia, " +
            "count(*) FILTER (WHERE opcion_id = 38) AS total_propia_pero_vive_fuera";
}
