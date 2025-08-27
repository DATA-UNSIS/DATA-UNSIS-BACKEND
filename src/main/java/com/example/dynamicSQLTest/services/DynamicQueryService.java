package com.example.dynamicSQLTest.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import com.example.dynamicSQLTest.DTOs.request.QueryRequest;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DynamicQueryService {

    @PersistenceContext
    private EntityManager entityManager;

    // Table aliases mapping
    private static final Map<String, String> TABLE_ALIAS_MAP = Map.ofEntries(
            Map.entry("grado_estudios", "ge"),
            Map.entry("opciones", "op"),
            Map.entry("roles", "r"),
            Map.entry("tipo_de_documento", "tdd"),
            Map.entry("tipo_preguntas", "tp"),
            Map.entry("usuarios", "u"),
            Map.entry("alumnos", "a"),
            Map.entry("becas", "b"),
            Map.entry("dependencia_economica", "de"),
            Map.entry("documentos", "doc"),
            Map.entry("formacion_academica", "fa"),
            Map.entry("hermanos", "h"),
            Map.entry("ingresos", "i"),
            Map.entry("lugar_procedencia", "lp"),
            Map.entry("preguntas", "p"),
            Map.entry("respuestas", "res"),
            Map.entry("respuestas_seleccion_multiple", "rsm"),
            Map.entry("rol_usuarios", "ru"),
            Map.entry("servicios", "s"),
            Map.entry("trayectoria_estudiantil", "te"),
            Map.entry("tutores", "t"),
            Map.entry("opciones_preguntas", "opp")
    );

    // Table relationships mapping
    private static final Map<String, String> TABLE_RELATIONSHIPS = Map.ofEntries(
            Map.entry("alumnos", "usuarios"),
            Map.entry("becas", "alumnos"),
            Map.entry("dependencia_economica", "alumnos"),
            Map.entry("documentos", "alumnos"),
            Map.entry("formacion_academica", "alumnos"),
            Map.entry("hermanos", "alumnos"),
            Map.entry("ingresos", "alumnos"),
            Map.entry("lugar_procedencia", "alumnos"),
            Map.entry("respuestas", "alumnos"),
            Map.entry("servicios", "alumnos"),
            Map.entry("trayectoria_estudiantil", "alumnos"),
            Map.entry("tutores", "alumnos"),
            Map.entry("rol_usuarios", "usuarios"),
            Map.entry("preguntas", "usuarios"),
            Map.entry("respuestas_seleccion_multiple", "respuestas"),
            Map.entry("opciones_preguntas", "preguntas")
    );

    // Join conditions mapping
    private static final Map<String, String> JOIN_CONDITIONS = Map.ofEntries(
            Map.entry("alumnos", "u.usuario = a.curp"),
            Map.entry("becas", "a.curp = b.curp"),
            Map.entry("dependencia_economica", "a.curp = de.curp"),
            Map.entry("documentos", "a.curp = doc.curp"),
            Map.entry("formacion_academica", "a.curp = fa.curp"),
            Map.entry("hermanos", "a.curp = h.curp"),
            Map.entry("ingresos", "a.curp = i.curp"),
            Map.entry("lugar_procedencia", "a.curp = lp.curp"),
            Map.entry("respuestas", "a.curp = res.curp"),
            Map.entry("servicios", "a.curp = s.curp"),
            Map.entry("trayectoria_estudiantil", "a.curp = te.curp"),
            Map.entry("tutores", "a.curp = t.curp"),
            Map.entry("rol_usuarios", "u.usuario = ru.usuario"),
            Map.entry("preguntas", "u.usuario = p.usuario"),
            Map.entry("respuestas_seleccion_multiple", "res.id_respuesta = rsm.respuesta_id"),
            Map.entry("opciones_preguntas", "p.id_pregunta = opp.id_pregunta"),
            Map.entry("documentos_tipo", "doc.id_tipo_doc = tdd.id_tipo_doc"),
            Map.entry("hermanos_grado", "h.id_grado_estudios = ge.id_grado_estudios"),
            Map.entry("tutores_grado", "t.id_grado_estudios = ge.id_grado_estudios"),
            Map.entry("preguntas_tipo", "p.id_tipo = tp.id_tipo"),
            Map.entry("respuestas_preguntas", "res.id_pregunta = p.id_pregunta"),
            Map.entry("rol_usuarios_roles", "ru.id_rol = r.id_rol"),
            Map.entry("opciones_preguntas_opciones", "opp.id_opcion = op.id_opcion"),
            Map.entry("respuestas_multiple_opciones", "rsm.opcion_id = op.id_opcion")
    );

    // Field to table mapping
    private static final Map<String, String> FIELD_TABLE_MAP = Map.ofEntries(
            // usuarios fields
            Map.entry("usuario", "usuarios"),
            Map.entry("contrasena", "usuarios"),
            Map.entry("estatus", "usuarios"),
            Map.entry("usuarios_nombre", "usuarios"),
            Map.entry("p_apellido", "usuarios"),
            Map.entry("s_apellido", "usuarios"),
            Map.entry("sexo_curp", "usuarios"),

            // alumnos fields
            Map.entry("curp", "alumnos"),
            Map.entry("carrera", "alumnos"),
            Map.entry("correo_electronico", "alumnos"),
            Map.entry("cual_discapacidad", "alumnos"),
            Map.entry("cual_grupo_etnico", "alumnos"),
            Map.entry("cual_lengua", "alumnos"),
            Map.entry("cual_nacionalidad", "alumnos"),
            Map.entry("cualotra_discapacidad", "alumnos"),
            Map.entry("cualestado_civil", "alumnos"),
            Map.entry("domicilio_miahuatlan", "alumnos"),
            Map.entry("entidad_nac", "alumnos"),
            Map.entry("estado_civil", "alumnos"),
            Map.entry("ficha_matricula_", "alumnos"),
            Map.entry("grupo", "alumnos"),
            Map.entry("grupo_etnico", "alumnos"),
            Map.entry("lengua_indigena", "alumnos"),
            Map.entry("lentes_graduacion", "alumnos"),
            Map.entry("n_hijos", "alumnos"),
            Map.entry("nacionalidad", "alumnos"),
            Map.entry("semestre", "alumnos"),
            Map.entry("sexo", "alumnos"),
            Map.entry("telefono", "alumnos"),
            Map.entry("tiene_discapacidad", "alumnos"),
            Map.entry("tipo_sangre", "alumnos"),

            // becas fields
            Map.entry("beca_manutencion", "becas"),
            Map.entry("conafe", "becas"),
            Map.entry("otro", "becas"),
            Map.entry("jovenes_escribiendo", "becas"),
            Map.entry("madre_soltera", "becas"),
            Map.entry("otra_beca", "becas"),
            Map.entry("semillas_talento", "becas"),

            // dependencia_economica fields
            Map.entry("dependencia_abuelo", "dependencia_economica"),
            Map.entry("dependencia_esposo", "dependencia_economica"),
            Map.entry("dependencia_hermano", "dependencia_economica"),
            Map.entry("dependencia_independiente", "dependencia_economica"),
            Map.entry("dependencia_madre", "dependencia_economica"),
            Map.entry("dependencia_otro", "dependencia_economica"),
            Map.entry("dependencia_padre", "dependencia_economica"),

            // documentos fields
            Map.entry("id_documento", "documentos"),
            Map.entry("documento_base64", "documentos"),
            Map.entry("formato", "documentos"),
            Map.entry("documentos_nombre", "documentos"),

            // formacion_academica fields
            Map.entry("area_conoc", "formacion_academica"),
            Map.entry("cual_sub_educativo", "formacion_academica"),
            Map.entry("distrito_esc", "formacion_academica"),
            Map.entry("entidad_esc", "formacion_academica"),
            Map.entry("esc_procedencia", "formacion_academica"),
            Map.entry("esp_bachillerato", "formacion_academica"),
            Map.entry("otra_entidad_esc", "formacion_academica"),
            Map.entry("sub_educativo", "formacion_academica"),

            // hermanos fields
            Map.entry("id_hermano", "hermanos"),
            Map.entry("hermanos_s_apellido", "hermanos"),
            Map.entry("hermanos_p_apellido", "hermanos"),
            Map.entry("hermanos_nombre", "hermanos"),

            // ingresos fields
            Map.entry("id_ingreso", "ingresos"),
            Map.entry("empresa_institucion", "ingresos"),
            Map.entry("funcion_desempena", "ingresos"),
            Map.entry("ingreso", "ingresos"),
            Map.entry("parentesco", "ingresos"),

            // lugar_procedencia fields
            Map.entry("coordenadas_domicilio", "lugar_procedencia"),
            Map.entry("cual_entidad", "lugar_procedencia"),
            Map.entry("distrito", "lugar_procedencia"),
            Map.entry("domicilio", "lugar_procedencia"),
            Map.entry("entidad", "lugar_procedencia"),
            Map.entry("localidad", "lugar_procedencia"),
            Map.entry("municipio", "lugar_procedencia"),
            Map.entry("region", "lugar_procedencia"),
            Map.entry("telefono_familiar", "lugar_procedencia"),

            // servicios fields
            Map.entry("agua_potable", "servicios"),
            Map.entry("drenaje", "servicios"),
            Map.entry("energia_electrica", "servicios"),
            Map.entry("instalacion_gas", "servicios"),
            Map.entry("internet", "servicios"),
            Map.entry("lavadora_ropa", "servicios"),
            Map.entry("microondas", "servicios"),
            Map.entry("refrigerador", "servicios"),
            Map.entry("tel_celular", "servicios"),
            Map.entry("tel_fijo", "servicios"),
            Map.entry("television", "servicios"),

            // trayectoria_estudiantil fields
            Map.entry("prom_media_superior_", "trayectoria_estudiantil"),
            Map.entry("prom_otros", "trayectoria_estudiantil"),
            Map.entry("prom_primaria", "trayectoria_estudiantil"),
            Map.entry("prom_secundaria", "trayectoria_estudiantil"),

            // Foreign key fields mapping
            Map.entry("id_grado_estudios", "grado_estudios"),
            Map.entry("id_tipo_doc", "tipo_de_documento"),
            Map.entry("id_tipo", "tipo_preguntas"),
            Map.entry("id_opcion", "opciones"),
            Map.entry("id_rol", "roles"),
            Map.entry("id_pregunta", "preguntas"),
            Map.entry("id_tutor", "tutores"),
            Map.entry("id_respuesta", "respuestas"),
            Map.entry("id_rol_usuario", "rol_usuarios"),
            Map.entry("id_opcion_pregunta", "opciones_preguntas")
    );

    public List<Map<String, Object>> executeDynamicQuery(QueryRequest queryRequest) {
        String finalQuery;
        Map<String, Object> parameters = new HashMap<>();

        if (queryRequest.getSelectFields() != null && queryRequest.getFromTables() != null) {
            finalQuery = buildQueryFromComponents(queryRequest, parameters);
        } else {
            throw new IllegalArgumentException("selectFields or fromTables error");
        }

        System.out.println("Query: " + finalQuery);
        System.out.println("Parameters: " + parameters);

        return executeNativeQuery(finalQuery, parameters);
    }

    private String buildQueryFromComponents(QueryRequest queryRequest, Map<String, Object> parameters) {
        StringBuilder queryBuilder = new StringBuilder();

        Map<String, String> tableAliases = generateTableAliases(queryRequest.getFromTables());

        // SELECT clause
        queryBuilder.append("SELECT ")
                .append(buildSelectClause(queryRequest.getSelectFields(), tableAliases));

        // FROM clause
        queryBuilder.append(" FROM ")
                .append(buildFromClause(queryRequest.getFromTables(), tableAliases));

        // JOIN clause
        String joinClauses = generateAutoJoins(queryRequest.getFromTables(), tableAliases);
        if (!joinClauses.isEmpty()) {
            queryBuilder.append(" ").append(joinClauses);
        }

        // WHERE clause
        if (queryRequest.getFilters() != null && !queryRequest.getFilters().isEmpty()) {
            String whereClause = buildWhereClause(queryRequest.getFilters(), parameters, tableAliases);
            if (!whereClause.isEmpty()) {
                queryBuilder.append(" WHERE ").append(whereClause);
            }
        }

        // GROUP BY clause
        if (queryRequest.getGroupBy() != null && !queryRequest.getGroupBy().isEmpty()) {
            queryBuilder.append(" GROUP BY ")
                    .append(buildGroupByClause(queryRequest.getGroupBy(), tableAliases));
        }

        // HAVING clause
        if (queryRequest.getHavingConditions() != null && !queryRequest.getHavingConditions().isEmpty()) {
            queryBuilder.append(" HAVING ")
                    .append(buildHavingClause(queryRequest.getHavingConditions(), parameters, tableAliases));
        }

        // ORDER BY clause
        if (queryRequest.getOrderBy() != null && !queryRequest.getOrderBy().isEmpty()) {
            queryBuilder.append(" ORDER BY ")
                    .append(buildOrderByClause(queryRequest.getOrderBy(), tableAliases));
        }

        // LIMIT clause
        if (queryRequest.getLimit() != null && queryRequest.getLimit() > 0) {
            queryBuilder.append(" LIMIT ").append(queryRequest.getLimit());
        }

        return queryBuilder.toString();
    }

    private Map<String, String> generateTableAliases(List<String> fromTables) {
        Map<String, String> aliases = new LinkedHashMap<>();
        for (String table : fromTables) {
            String tableName = table.trim();
            String alias = TABLE_ALIAS_MAP.getOrDefault(tableName, tableName.substring(0, 1));
            aliases.put(tableName, alias);
        }
        return aliases;
    }

    private String buildSelectClause(List<String> selectFields, Map<String, String> tableAliases) {
        List<String> processedFields = new ArrayList<>();

        for (String field : selectFields) {
            processedFields.add(processField(field, tableAliases));
        }

        return String.join(", ", processedFields);
    }

    private String processField(String field, Map<String, String> tableAliases) {
        if (field.toLowerCase().matches("(count|sum|avg|min|max)\\(.*\\)")) {
            return processAggregateFunction(field, tableAliases);
        }

        if (field.contains(".")) {
            return field;
        }

        String tableName = detectTableForField(field, tableAliases);
        if (tableName != null) {
            String alias = tableAliases.get(tableName);
            return alias + "." + field;
        }

        String firstTable = tableAliases.keySet().iterator().next();
        String firstAlias = tableAliases.get(firstTable);
        return firstAlias + "." + field;
    }

    private String processAggregateFunction(String field, Map<String, String> tableAliases) {
        Pattern pattern = Pattern.compile("(count|sum|avg|min|max)\\((.*?)\\)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(field.toLowerCase());

        if (matcher.find()) {
            String function = matcher.group(1);
            String innerField = matcher.group(2).trim();

            String processedInnerField;
            if (innerField.equals("*")) {
                processedInnerField = "*";
            } else {
                processedInnerField = processField(innerField, tableAliases);
            }

            return function.toUpperCase() + "(" + processedInnerField + ")";
        }

        return field;
    }

    private String detectTableForField(String field, Map<String, String> tableAliases) {
        if (FIELD_TABLE_MAP.containsKey(field)) {
            String suggestedTable = FIELD_TABLE_MAP.get(field);
            if (tableAliases.containsKey(suggestedTable)) {
                return suggestedTable;
            }
        }

        if (field.endsWith("_id")) {
            String potentialTable = field.substring(0, field.length() - 3);
            if (tableAliases.containsKey(potentialTable)) {
                return potentialTable;
            }
            String pluralTable = potentialTable + "s";
            if (tableAliases.containsKey(pluralTable)) {
                return pluralTable;
            }
        }

        for (String table : tableAliases.keySet()) {
            if (field.startsWith(table.substring(0, 1)) ||
                    field.contains(table.substring(0, 3))) {
                return table;
            }
        }

        return null;
    }

    private String buildFromClause(List<String> fromTables, Map<String, String> tableAliases) {
        List<String> tablesWithAliases = new ArrayList<>();
        String table = fromTables.getFirst();
        String alias = tableAliases.get(table);
        tablesWithAliases.add(table + " " + alias);

        return String.join(", ", tablesWithAliases);
    }

    private String generateAutoJoins(List<String> fromTables, Map<String, String> tableAliases) {
        if (fromTables.size() <= 1) {
            return "";
        }

        StringBuilder joins = new StringBuilder();
        List<String> tables = new ArrayList<>(fromTables);
        String mainTable = tables.get(0);
        String mainAlias = tableAliases.get(mainTable);

        for (int i = 1; i < tables.size(); i++) {
            String currentTable = tables.get(i);
            String currentAlias = tableAliases.get(currentTable);

            String joinCondition = findJoinCondition(mainTable, currentTable, mainAlias, currentAlias);

            if (joinCondition != null) {
                joins.append(" INNER JOIN ").append(currentTable).append(" ").append(currentAlias)
                        .append(" ON ").append(joinCondition);
            } else {
                joinCondition = findAnyJoinCondition(mainTable, currentTable, mainAlias, currentAlias);
                if (joinCondition != null) {
                    joins.append(" INNER JOIN ").append(currentTable).append(" ").append(currentAlias)
                            .append(" ON ").append(joinCondition);
                }
            }
        }

        return joins.toString();
    }

    private String findJoinCondition(String table1, String table2, String alias1, String alias2) {
        String key1 = table1;
        String key2 = table2;

        if (JOIN_CONDITIONS.containsKey(key1)) {
            return JOIN_CONDITIONS.get(key1).replace(table1 + ".", alias1 + ".")
                    .replace(table2 + ".", alias2 + ".");
        }

        if (JOIN_CONDITIONS.containsKey(key2)) {
            return JOIN_CONDITIONS.get(key2).replace(table1 + ".", alias1 + ".")
                    .replace(table2 + ".", alias2 + ".");
        }

        return null;
    }

    private String findAnyJoinCondition(String table1, String table2, String alias1, String alias2) {
        if (TABLE_RELATIONSHIPS.get(table1) != null && TABLE_RELATIONSHIPS.get(table2) != null) {
            String parent1 = TABLE_RELATIONSHIPS.get(table1);
            String parent2 = TABLE_RELATIONSHIPS.get(table2);

            if (parent1.equals(parent2)) {
                return alias1 + ".curp = " + alias2 + ".curp";
            }

            if (parent1.equals("alumnos") && parent2.equals("usuarios")) {
                return alias1 + ".curp = " + alias2 + ".usuario";
            }

            if (parent1.equals("usuarios") && parent2.equals("alumnos")) {
                return alias1 + ".usuario = " + alias2 + ".curp";
            }
        }

        return null;
    }

    private String buildWhereClause(Map<String, Object> filters, Map<String, Object> parameters, Map<String, String> tableAliases) {
        List<String> conditions = new ArrayList<>();
        int paramCount = getCurrentParamCount(parameters);

        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            if (entry.getValue() != null) {
                String field = processField(entry.getKey(), tableAliases);
                String paramName = "param" + (++paramCount);

                if (entry.getValue().toString().contains(",")) {
                    String[] values = entry.getValue().toString().split(",");
                    List<String> cleanValues = new ArrayList<>();
                    for (String value : values) {
                        String cleanValue = value.trim().replace("[", "").replace("]", "");
                        if (!cleanValue.isEmpty()) {
                            cleanValues.add(cleanValue);
                        }
                    }

                    if (!cleanValues.isEmpty()) {
                        String placeholders = String.join(",", Collections.nCopies(cleanValues.size(), "?"));
                        conditions.add(field + " IN (" + placeholders + ")");
                        for (int i = 0; i < cleanValues.size(); i++) {
                            parameters.put(paramName + "_" + i, cleanValues.get(i));
                        }
                    }
                } else {
                    conditions.add(field + " = :" + paramName);
                    parameters.put(paramName, entry.getValue());
                }
            }
        }

        return String.join(" AND ", conditions);
    }

    private String buildGroupByClause(List<String> groupByFields, Map<String, String> tableAliases) {
        List<String> processedFields = new ArrayList<>();
        for (String field : groupByFields) {
            processedFields.add(processField(field, tableAliases));
        }
        return String.join(", ", processedFields);
    }

    private String buildHavingClause(Map<String, Object> havingConditions, Map<String, Object> parameters, Map<String, String> tableAliases) {
        List<String> conditions = new ArrayList<>();
        int paramCount = getCurrentParamCount(parameters);

        for (Map.Entry<String, Object> entry : havingConditions.entrySet()) {
            String field = processField(entry.getKey(), tableAliases);
            String paramName = "having_param" + (++paramCount);
            conditions.add(field + " = :" + paramName);
            parameters.put(paramName, entry.getValue());
        }

        return String.join(" AND ", conditions);
    }

    private String buildOrderByClause(List<String> orderByFields, Map<String, String> tableAliases) {
        List<String> processedFields = new ArrayList<>();

        for (String field : orderByFields) {
            String direction = "";
            String fieldName = field;

            if (field.toUpperCase().endsWith(" ASC")) {
                fieldName = field.substring(0, field.length() - 4).trim();
                direction = " ASC";
            } else if (field.toUpperCase().endsWith(" DESC")) {
                fieldName = field.substring(0, field.length() - 5).trim();
                direction = " DESC";
            }

            processedFields.add(processField(fieldName, tableAliases) + direction);
        }

        return String.join(", ", processedFields);
    }

    private int getCurrentParamCount(Map<String, Object> parameters) {
        return (int) parameters.keySet().stream()
                .filter(key -> key.startsWith("param"))
                .count();
    }

    private List<Map<String, Object>> executeNativeQuery(String query, Map<String, Object> parameters) {
        try {
            Query nativeQuery = entityManager.createNativeQuery(query);

            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                nativeQuery.setParameter(entry.getKey(), entry.getValue());
            }

            List<Object[]> results = nativeQuery.getResultList();

            if (results.isEmpty()) {
                return Collections.emptyList();
            }

            Object firstResult = results.get(0);
            if (firstResult instanceof Object[]) {
                return transformArrayResults(results);
            } else {
                return transformScalarResults(results, query);
            }

        } catch (Exception e) {
            System.err.println("Query execution error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Query execution failed: " + e.getMessage(), e);
        }
    }

    private List<Map<String, Object>> transformArrayResults(List<Object[]> results) {
        List<Map<String, Object>> transformed = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> rowMap = new LinkedHashMap<>();

            for (int i = 0; i < row.length; i++) {
                rowMap.put("column_" + (i + 1), row[i]);
            }
            transformed.add(rowMap);
        }

        return transformed;
    }

    private List<Map<String, Object>> transformScalarResults(List<Object[]> results, String query) {
        List<Map<String, Object>> transformed = new ArrayList<>();

        String columnName = extractColumnNameFromQuery(query);

        for (Object result : results) {
            Map<String, Object> rowMap = new LinkedHashMap<>();
            rowMap.put(columnName, result);
            transformed.add(rowMap);
        }

        return transformed;
    }

    private String extractColumnNameFromQuery(String query) {
        String lowerQuery = query.toLowerCase();

        if (lowerQuery.contains("count(")) return "count";
        if (lowerQuery.contains("sum(")) return "sum";
        if (lowerQuery.contains("avg(")) return "average";
        if (lowerQuery.contains("min(")) return "minimum";
        if (lowerQuery.contains("max(")) return "maximum";

        if (lowerQuery.contains("select") && lowerQuery.contains("from")) {
            String selectPart = query.substring(query.toLowerCase().indexOf("select") + 6,
                    query.toLowerCase().indexOf("from"));
            String[] parts = selectPart.split(",");
            if (parts.length > 0) {
                String lastPart = parts[parts.length - 1].trim();
                if (lastPart.contains(" as ")) {
                    return lastPart.substring(lastPart.lastIndexOf(" as ") + 4).trim();
                }
                if (lastPart.contains(".")) {
                    return lastPart.substring(lastPart.lastIndexOf(".") + 1);
                }
                return lastPart;
            }
        }

        return "result";
    }
}