package com.example.dynamicSQLTest.processors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.dynamicSQLTest.DTOs.request.GeneralQueryRequest;
import com.example.dynamicSQLTest.DTOs.response.QueryResponse;
import com.example.dynamicSQLTest.common.GeneralQuerysConstants;
import com.example.dynamicSQLTest.enums.EHouseholdServices;
import com.example.dynamicSQLTest.enums.ETitles;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
@Service
public class TitlesLogicProcessor {

    @PersistenceContext
    private EntityManager entityManager;

    public QueryResponse executeNativeQuery(ETitles title, GeneralQueryRequest request, List<String> tables, String defaultQuery, String filter, Class<? extends Enum<?>> enumType) {
        QueryResponse results = new QueryResponse();
        Query nativeQuery = null;
        String compoundQuery;

        Map<String, Object> dataList = new HashMap<>();
        try{
            //todas las carreras, todos los semestres y todos los sexos (No especifica ninguno == null)
            if(request.getMajors() == null && request.getSemesters() == null && request.getSexo() == null){
                nativeQuery = entityManager.createNativeQuery(defaultQuery.toString());
                Object[] result = (Object[]) nativeQuery.getSingleResult();
                results.setTitle(title);
                for(int i=0; i<result.length; i++){
                    dataList.put(EHouseholdServices.values()[i].toString(), result[i]);
                }
                results.setData(dataList);
                return results;
            }else{
                compoundQuery = getCompoundQuery(request, tables, filter);
                nativeQuery = entityManager.createNativeQuery(compoundQuery);

                @SuppressWarnings("unchecked")
                List<Object[]> resultList = nativeQuery.getResultList();
                results.setTitle(title);
                results.setData(getResultsData(request, dataList, resultList, results, enumType).getData());
            }
        }catch(Exception e){
            System.err.println("Query execution error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Query execution failed: " + e.getMessage(), e);
        }
        return results;
    }

    private QueryResponse getResultsData(GeneralQueryRequest request, Map<String, Object> dataList, List<Object[]> resultList, QueryResponse results, Class<? extends Enum<?>> enumType) {
        if(request.getMajors() != null && request.getSemesters() != null && request.getSexo() != null){
            results.setData(queryProcess(resultList, dataList, enumType));
            return results;
        }else
        if(request.getMajors() != null && request.getSemesters() != null && request.getSexo() == null){
            results.setData(queryProcess(resultList, dataList, enumType));
            return results;
        }else
        if(request.getMajors() != null && request.getSemesters() == null && request.getSexo() != null){
            results.setData(queryProcess(resultList, dataList, enumType));
            return results;
        }else
        if(request.getMajors() != null && request.getSemesters() == null && request.getSexo() == null){
            results.setData(queryProcess(resultList, dataList, enumType));
            return results;
        }else
        if(request.getMajors() == null && request.getSemesters() != null && request.getSexo() != null){
            results.setData(queryProcess(resultList, dataList, enumType));
            return results;
        }else
        if(request.getMajors() == null && request.getSemesters() == null && request.getSexo() != null){
            results.setData(queryProcess(resultList, dataList, enumType));
            return results;
        }else
        if(request.getMajors() == null && request.getSemesters() != null && request.getSexo() == null){
            results.setData(queryProcess(resultList, dataList, enumType));
            return results;
        }

        return results;
    }
    //Procesa los resultados de la consulta y los mapea al formato esperado
    private Map<String, Object> queryProcess(List<Object[]> resultList, Map<String, Object> dataList, Class<? extends Enum<?>> enumType){
        for(Object[] row : resultList) {
            for(int i=0; i<row.length; i++){
                dataList.put(enumType.getEnumConstants()[i].toString(), row[i]);
            }
        }
        return dataList;
    }
    //Procesa la distribución por municipios, retorna todos los municipios existentes en BD y su total
    public QueryResponse executeQueryDistributionNullEnum(ETitles title, String defaultQuery) {
        try {
            QueryResponse results = new QueryResponse();
            Map<String, Object> dataList = new HashMap<>();

            Query nativeQuery = entityManager.createNativeQuery(defaultQuery);
            @SuppressWarnings("unchecked")
            List<Object[]> result = nativeQuery.getResultList();
            results.setTitle(title);
            System.out.println(defaultQuery);
            for (Object[] row : result) {
                // Usar el primer elemento como clave (nombre) y el segundo como valor (total)
                String elemento = (String) row[0];
                Object total = row[1];
                dataList.put(elemento, total);
            }
            results.setData(dataList);
            return results;
        }catch (Exception e){
            System.err.println("Query execution error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Query execution failed: " + e.getMessage(), e);
        }
    }

    private String getCompoundQuery(GeneralQueryRequest request, List<String> tables, String filter){
        StringBuilder query = new StringBuilder();
        List<String> majors = request.getMajors();
        List<String> semesters = request.getSemesters();
        String sex = request.getSexo();


        //Carrera + semestre + sexo (Especifica al menos uno)
        if(request.getMajors() != null && request.getSemesters() != null && request.getSexo() != null){
            query.append("SELECT ").append(filter);
            query.append(" FROM ").append(String.join(", ", tables));
            query.append(GeneralQuerysConstants.CLAUSULE_WHERE_MAJORS);
            String concatenatedMajors = "'" + String.join("', '", majors) + "'";
            query.append(" ("+concatenatedMajors+") ").append(" AND ").append(GeneralQuerysConstants.CLAUSULE_SEMESTERS);
            String concatenatedSemesters = "'" + String.join("', '", semesters) + "'";
            query.append(" ("+concatenatedSemesters+") ").append(" AND ");
            query. append(GeneralQuerysConstants.CLAUSULE_SEX).append(" ('"+sex+"') ").append(";");
            System.out.println("Carrera, semestre, sexo (Especifica al menos uno): "+"\n"+query+"\n\n");
            return query.toString();
        }else
            //carrera + semestre
            if(request.getMajors() != null && request.getSemesters() != null && request.getSexo() == null){
                query.append("SELECT ").append(filter);
                query.append(" FROM ").append(String.join(" JOIN ", tables));
                String joinClause = String.format(GeneralQuerysConstants.JOIN_ON_CURP, tables.get(0), tables.get(1));
                query.append(joinClause);
                query.append(GeneralQuerysConstants.CLAUSULE_WHERE_MAJORS);
                String concatenatedMajors = "'" + String.join("', '", majors) + "'";
                query.append(" ("+concatenatedMajors+") ").append(" AND ").append(GeneralQuerysConstants.CLAUSULE_SEMESTERS);
                String concatenatedSemesters = "'" + String.join("', '", semesters) + "'";
                query.append(" ("+concatenatedSemesters+") ").append(";");
                System.out.println("carrera + semestre: "+"\n"+query+"\n\n");
                return query.toString();
            }else
                //carreras + sexo
                if(request.getMajors() != null && request.getSemesters() == null && request.getSexo() != null){
                    query.append("SELECT ").append(filter);
                    query.append(" FROM ").append(String.join(" JOIN ", tables));
                    String joinClause = String.format(GeneralQuerysConstants.JOIN_ON_CURP, tables.get(0), tables.get(1));
                    query.append(joinClause);
                    query.append(GeneralQuerysConstants.CLAUSULE_WHERE_MAJORS);
                    String concatenatedMajors = "'" + String.join("', '", majors) + "'";
                    query.append(" ("+concatenatedMajors+") ").append(" AND ");
                    query. append(GeneralQuerysConstants.CLAUSULE_SEX).append(" ('"+sex+"') ").append(";");
                    System.out.println("carreras + sexo: "+"\n"+query+"\n\n");
                    return query.toString();
                }else
                    //carrera
                    if(request.getMajors() != null && request.getSemesters() == null && request.getSexo() == null){
                        query.append("SELECT ").append(filter);
                        query.append(" FROM ").append(String.join(" JOIN ", tables));
                        String joinClause = String.format(GeneralQuerysConstants.JOIN_ON_CURP, tables.get(0), tables.get(1));
                        query.append(joinClause);
                        query.append(GeneralQuerysConstants.CLAUSULE_WHERE_MAJORS);
                        String concatenatedFilters = "'" + String.join("', '", majors) + "'";
                        query.append(" ("+concatenatedFilters+") ").append(";");
                        System.out.println("carreras: "+"\n"+query+"\n\n");
                        return query.toString();
                    }else
                        //semestres + sexo
                        if(request.getMajors() == null && request.getSemesters() != null && request.getSexo() != null){
                            query.append("SELECT ").append(filter);
                            query.append(" FROM ").append(String.join(" JOIN ", tables));
                            String joinClause = String.format(GeneralQuerysConstants.JOIN_ON_CURP, tables.get(0), tables.get(1));
                            query.append(joinClause);
                            query.append(" WHERE ").append(GeneralQuerysConstants.CLAUSULE_SEMESTERS);
                            String concatenatedSemesters = "'" + String.join("', '", semesters) + "'";
                            query.append(" ("+concatenatedSemesters+") ").append(" AND ");
                            query. append(GeneralQuerysConstants.CLAUSULE_SEX).append(" ('"+sex+"') ").append(";");
                            System.out.println("semestres + sexo: "+"\n"+query+"\n\n");
                            return query.toString();
                        }else
                            //sexo
                            if(request.getMajors() == null && request.getSemesters() == null && request.getSexo() != null){
                                query.append("SELECT ").append(filter);
                                query.append(" FROM ").append(String.join(" JOIN ", tables));
                                String joinClause = String.format(GeneralQuerysConstants.JOIN_ON_CURP, tables.get(0), tables.get(1));
                                query.append(joinClause);
                                query.append(" WHERE ");
                                query. append(GeneralQuerysConstants.CLAUSULE_SEX).append(" ('"+sex+"') ").append(";");
                                System.out.println("sexo: "+"\n"+query+"\n\n");
                                return query.toString();
                            }else
                            if(request.getMajors() == null && request.getSemesters() != null && request.getSexo() == null){
                                query.append("SELECT ").append(filter);
                                query.append(" FROM ").append(String.join(" JOIN ", tables));
                                String joinClause = String.format(GeneralQuerysConstants.JOIN_ON_CURP, tables.get(0), tables.get(1));
                                query.append(joinClause);
                                query.append(" WHERE ");
                                String concatenatedSemesters = "'" + String.join("', '", semesters) + "'";
                                query. append(GeneralQuerysConstants.CLAUSULE_SEMESTERS).append(" ("+concatenatedSemesters+") ").append(";");
                                System.out.println("semestres: "+"\n"+query+"\n\n");
                                return query.toString();
                            }
        return query.toString();
    }

}
