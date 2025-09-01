package com.example.dynamicSQLTest.services.generalServices;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.dynamicSQLTest.DTOs.request.GeneralQueryRequest;
import com.example.dynamicSQLTest.DTOs.response.QueryResponse;
import com.example.dynamicSQLTest.DTOs.utils.DataDTO;
import com.example.dynamicSQLTest.common.GeneralQuerysConstants;
import com.example.dynamicSQLTest.enums.EHouseholdServices;
import com.example.dynamicSQLTest.enums.ETitles;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
@Service
public class HouseHoldServicesService {

    @PersistenceContext
    private EntityManager entityManager;

    public QueryResponse executeNativeQuery(ETitles title, GeneralQueryRequest request){
        QueryResponse results = new QueryResponse();
        DataDTO dataDto = new DataDTO(); 
        Query nativeQuery = null;
        String compoundQuery;

        Map<String, Object> dataList = new HashMap<>();
        try{
            //todas las carreras, todos los semestres y todos los sexos (No especifica ninguno)
            if(request.getMajor().getMajors().size()<=0 && request.getSemester().getSemesters().size()<=0 && request.getSexo().isEmpty()){
                nativeQuery = entityManager.createNativeQuery(GeneralQuerysConstants.COUNT_HOUSE_HOULD_SERVICES);
                Object[] result = (Object[]) nativeQuery.getSingleResult();
                results.setTitle(title);
                for(int i=0; i<result.length; i++){
                    dataList.put(EHouseholdServices.values()[i].toString(), result[i]);
                }
                results.setData(dataList);
                return results;
            }else{
                compoundQuery=getCompoundQuery(request);
                nativeQuery = entityManager.createNativeQuery(compoundQuery);
                
                @SuppressWarnings("unchecked")
                List<Object[]> resultList = nativeQuery.getResultList();
                results.setTitle(title);
                results.setData(getResultsData(request, dataDto, dataList, resultList, results).getData());
            }
        }catch(Exception e){
            System.err.println("Query execution error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Query execution failed: " + e.getMessage(), e);
        }
        return results;
    }

    private QueryResponse getResultsData(GeneralQueryRequest request, DataDTO dataDto, Map<String, Object> dataList, List<Object[]> resultList, QueryResponse results){
                if(request.getMajor().getMajors().size()>0 && request.getSemester().getSemesters().size()>0 && !request.getSexo().isEmpty()){
                    results.setData(queryProcessMajorSemesterSex(resultList, dataList));
                    return results;
                }else
                if(request.getMajor().getMajors().size()>0 && request.getSemester().getSemesters().size()>0 && request.getSexo().isEmpty()){
                    results.setData(queryProcessMajorSemester(resultList, dataList));
                    return results;
                }else
                if(request.getMajor().getMajors().size()>0 && request.getSemester().getSemesters().size()<=0 && !request.getSexo().isEmpty()){
                    results.setData(queryProcessMajorSex(resultList, dataList));
                    return results;
                }else
                if(request.getMajor().getMajors().size()>0 && request.getSemester().getSemesters().size()<=0 && request.getSexo().isEmpty()){
                    results.setData(queryProcessMajor(resultList, dataList));
                    return results;
                }else
                if(request.getMajor().getMajors().size()<=0 && request.getSemester().getSemesters().size()>0 && !request.getSexo().isEmpty()){
                    results.setData(queryProcessSemesterSex(resultList, dataList));
                    return results;
                }else
                if(request.getMajor().getMajors().size()<=0 && request.getSemester().getSemesters().size()<=0 && !request.getSexo().isEmpty()){
                    results.setData(queryProcessSex(resultList, dataList));
                    return results;
                }else
                if(request.getMajor().getMajors().size()<=0 && request.getSemester().getSemesters().size()>0 && request.getSexo().isEmpty()){
                    results.setData(queryProcessSemester(resultList, dataList));
                    return results;
                }

                return results;
    }
    //Carrera + semestre + sexo
    @SuppressWarnings("unchecked")
    private Map<String, Object> queryProcessMajorSemesterSex(List<Object[]> resultList, Map<String, Object> dataList) {
        for (Object[] row : resultList) {
            String carrera = (String) row[0];   // columna 0 = carrera
            String semestre = (String) row[1];  // columna 1 = semestre
            String sexo = (String) row[2];      // columna 2 = sexo

            // Recuperar el mapa de la carrera, o crear uno nuevo
            Map<String, Object> carreraData = (Map<String, Object>) dataList.getOrDefault(carrera, new HashMap<>());

            // Recuperar el mapa del semestre dentro de la carrera
            Map<String, Object> semestreData = (Map<String, Object>) carreraData.getOrDefault(semestre, new HashMap<>());

            // Mapa de servicios para este sexo
            Map<String, Object> sexoData = new HashMap<>();

            // Procesar los servicios (columnas a partir de la 3 en adelante)
            for (int i = 3; i < row.length; i++) {
                sexoData.put(EHouseholdServices.values()[i - 3].toString(), row[i]);
            }

            // Guardar sexo dentro del semestre
            semestreData.put(sexo, sexoData);

            // Actualizar carrera y dataList
            carreraData.put(semestre, semestreData);
            dataList.put(carrera, carreraData);
        }
        return dataList;
    }

    //Carrera + semestre
   private Map<String, Object> queryProcessMajorSemester(List<Object[]> resultList, Map<String, Object> dataList) {
        for (Object[] row : resultList) {
            String carrera = (String) row[0];   // columna 0 = carrera
            String semestre = (String) row[1]; // columna 1 = semestre

            // Obtenemos el mapa de esa carrera (o uno nuevo)
            @SuppressWarnings("unchecked")
            Map<String, Object> carreraData = (Map<String, Object>) dataList.getOrDefault(carrera, new HashMap<>());

            // Mapa para ese semestre
            Map<String, Object> semestreData = new HashMap<>();

            // Procesar servicios (las columnas a partir del índice 2)
            for (int i = 2; i < row.length; i++) {
                semestreData.put(EHouseholdServices.values()[i - 2].toString(), row[i]);
            }

            // Guardar este semestre dentro de la carrera
            carreraData.put(semestre, semestreData);

            // Actualizar la entrada de la carrera
            dataList.put(carrera, carreraData);
        }
        return dataList;
    }


      // Carrera + Sexo
    private Map<String, Object> queryProcessMajorSex(List<Object[]> resultList, Map<String, Object> dataList){
    for(Object[] row : resultList) {
        String carrera = (String) row[0]; // carrera
        String sexo    = (String) row[1]; // sexo
        String key     = carrera + " - " + sexo;

        Map<String, Object> carreraData = new HashMap<>();
            for(int i = 2; i < row.length; i++) { // Los COUNT empiezan en la tercera columna (índice 2)
                carreraData.put(EHouseholdServices.values()[i-2].toString(), row[i]);
            }
            dataList.put(key, carreraData);
        }
        return dataList;
    }


    // Semestre + Sexo
    private Map<String, Object> queryProcessSemesterSex(List<Object[]> resultList, Map<String, Object> dataList){
        for(Object[] row : resultList) {
            String semestre = (String) row[0];  // semestre
            String sexo     = (String) row[1];  // sexo
            String key      = semestre + " - " + sexo;

            Map<String, Object> semestreData = new HashMap<>();
            for(int i=2; i<row.length; i++){ // Saltamos semestre (0) y sexo (1)
                semestreData.put(EHouseholdServices.values()[i-2].toString(), row[i]);
            }
            dataList.put(key, semestreData);
        }
        return dataList;
    }

    // Solo Sexo
    private Map<String, Object> queryProcessSex(List<Object[]> resultList, Map<String, Object> dataList){
        for(Object[] row : resultList) {
            String sexo = (String) row[0];  // Tercera columna: sexo

            Map<String, Object> sexoData = new HashMap<>();
            for(int i=1; i<row.length; i++){ // Saltamos sexo (2)
                sexoData.put(EHouseholdServices.values()[i-1].toString(), row[i]);
            }
            dataList.put(sexo, sexoData);
        }
        return dataList;
    }

    //solo semestres
    private Map<String, Object> queryProcessSemester(List<Object[]> resultList, Map<String, Object> dataList){
        for(Object[] row : resultList){
            String semestre = (String) row[0]; //semestre
            Map<String, Object> semestreData = new HashMap<>();
            // Procesar servicios (saltar primera columna que es semestre)
            for(int i=1; i<row.length; i++){
                semestreData.put(EHouseholdServices.values()[i-1].toString(), row[i]);
            }
            // Agregar datos de este semestre
            dataList.put(semestre, semestreData);
        }
        return dataList;
    }

    //Solo carrera
    private Map<String, Object> queryProcessMajor(List<Object[]> resultList, Map<String, Object> dataList){
        for(Object[] row : resultList) {
                    String carrera = (String) row[0]; // Primera columna es carrera
                    Map<String, Object> carreraData = new HashMap<>();
                    // Procesar servicios (saltar primera columna que es carrera)
                    for(int i=1; i<row.length; i++){
                        carreraData.put(EHouseholdServices.values()[i-1].toString(), row[i]);
                    }
                    // Agregar datos de esta carrera
                    dataList.put(carrera, carreraData);
                }
        return dataList;
    }

    private String getCompoundQuery(GeneralQueryRequest request){
        StringBuilder query = new StringBuilder();
        List<String> tables = Arrays.asList("servicios", "alumnos");
        List<String> majors = request.getMajor().getMajors();
        List<String> semesters = request.getSemester().getSemesters();
        String sex = request.getSexo();

        //Carrera + semestre + sexo (Especifica al menos uno)
        if(request.getMajor().getMajors().size()>0 && request.getSemester().getSemesters().size()>0 && !request.getSexo().isEmpty()){
            query.append("SELECT ").append("carrera, semestre, sexo, "+GeneralQuerysConstants.FILTERS_COUNT_HOUSE_HOULD_SERVICES);
            query.append(" FROM ").append(String.join(", ", tables));
            query.append(GeneralQuerysConstants.CLAUSULE_WHERE_H_H_S_C);
            String concatenatedMajors = "'" + String.join("', '", majors) + "'";
            query.append(" ("+concatenatedMajors+") ").append(" AND ").append(GeneralQuerysConstants.CLAUSULE_H_H_S_S);
            String concatenatedSemesters = "'" + String.join("', '", semesters) + "'";
            query.append(" ("+concatenatedSemesters+") ").append(" AND ");
            query. append(GeneralQuerysConstants.CLAUSULE_H_H_S_SEX).append(" ('"+sex+"') ").append("GROUP BY carrera, semestre, sexo ORDER BY carrera, semestre, sexo;");
            System.out.println("Carrera, semestre, sexo (Especifica al menos uno): "+"\n"+query+"\n\n");
            return query.toString();
        }else
        //carrera + semestre
        if(request.getMajor().getMajors().size()>0 && request.getSemester().getSemesters().size()>0 && request.getSexo().isEmpty()){
            query.append("SELECT ").append("carrera, semestre, "+GeneralQuerysConstants.FILTERS_COUNT_HOUSE_HOULD_SERVICES);
            query.append(" FROM ").append(String.join(", ", tables));
            query.append(GeneralQuerysConstants.CLAUSULE_WHERE_H_H_S_C);
            String concatenatedMajors = "'" + String.join("', '", majors) + "'";
            query.append(" ("+concatenatedMajors+") ").append(" AND ").append(GeneralQuerysConstants.CLAUSULE_H_H_S_S);
            String concatenatedSemesters = "'" + String.join("', '", semesters) + "'";
            query.append(" ("+concatenatedSemesters+") ").append("GROUP BY carrera, semestre ORDER BY carrera, semestre;");
            System.out.println("carrera, semestre, no sexo: "+"\n"+query+"\n\n");
            return query.toString();
        }else 
        //carreras + sexo
        if(request.getMajor().getMajors().size()>0 && request.getSemester().getSemesters().size()<=0 && !request.getSexo().isEmpty()){
            query.append("SELECT ").append("carrera, sexo, "+GeneralQuerysConstants.FILTERS_COUNT_HOUSE_HOULD_SERVICES);
            query.append(" FROM ").append(String.join(", ", tables));
            query.append(GeneralQuerysConstants.CLAUSULE_WHERE_H_H_S_C);
            String concatenatedMajors = "'" + String.join("', '", majors) + "'";
            query.append(" ("+concatenatedMajors+") ").append(" AND ");
            query. append(GeneralQuerysConstants.CLAUSULE_H_H_S_SEX).append(" ('"+sex+"') ").append("GROUP BY carrera, sexo ORDER BY carrera, sexo;");
            System.out.println("carreras + sexo: "+"\n"+query+"\n\n");
            return query.toString();
        }else
        //carrera
        if(request.getMajor().getMajors().size()>0 && request.getSemester().getSemesters().size()<=0 && request.getSexo().isEmpty()){
            query.append("SELECT ").append(" carrera, "+GeneralQuerysConstants.FILTERS_COUNT_HOUSE_HOULD_SERVICES);
            query.append(" FROM ").append(String.join(", ", tables));
            query.append(GeneralQuerysConstants.CLAUSULE_WHERE_H_H_S_C);
            String concatenatedFilters = "'" + String.join("', '", majors) + "'";
            query.append(" ("+concatenatedFilters+") ").append(" GROUP BY carrera ORDER BY carrera;");
            System.out.println("carreras: "+"\n"+query+"\n\n");
            return query.toString();
        }else
        //semestres + sexo
        if(request.getMajor().getMajors().size()<=0 && request.getSemester().getSemesters().size()>0 && !request.getSexo().isEmpty()){
            query.append("SELECT ").append("semestre, sexo, "+GeneralQuerysConstants.FILTERS_COUNT_HOUSE_HOULD_SERVICES);
            query.append(" FROM ").append(String.join(", ", tables)).append(" WHERE ").append(GeneralQuerysConstants.CLAUSULE_H_H_S_S);
            String concatenatedSemesters = "'" + String.join("', '", semesters) + "'";
            query.append(" ("+concatenatedSemesters+") ").append(" AND ");
            query. append(GeneralQuerysConstants.CLAUSULE_H_H_S_SEX).append(" ('"+sex+"') ").append("GROUP BY semestre, sexo ORDER BY semestre, sexo;");
            System.out.println("semestres + sexo: "+"\n"+query+"\n\n");
            return query.toString();
        }else
        //sexo
        if(request.getMajor().getMajors().size()<=0 && request.getSemester().getSemesters().size()<=0 && !request.getSexo().isEmpty()){
            query.append("SELECT ").append("sexo, "+GeneralQuerysConstants.FILTERS_COUNT_HOUSE_HOULD_SERVICES);
            query.append(" FROM ").append(String.join(", ", tables)).append(" WHERE ");
            query. append(GeneralQuerysConstants.CLAUSULE_H_H_S_SEX).append(" ('"+sex+"') ").append("GROUP BY sexo ORDER BY sexo;");
            System.out.println("sexo: "+"\n"+query+"\n\n");
            return query.toString();
        }else
        if(request.getMajor().getMajors().size()<=0 && request.getSemester().getSemesters().size()>0 && request.getSexo().isEmpty()){
            query.append("SELECT ").append("semestre, "+GeneralQuerysConstants.FILTERS_COUNT_HOUSE_HOULD_SERVICES);
            query.append(" FROM ").append(String.join(", ", tables)).append(" WHERE ");
            String concatenatedSemesters = "'" + String.join("', '", semesters) + "'";
            query. append(GeneralQuerysConstants.CLAUSULE_H_H_S_S).append(" ("+concatenatedSemesters+") ").append("GROUP BY semestre ORDER BY semestre;");
            System.out.println("semestres: "+"\n"+query+"\n\n");
            return query.toString();
        }
        
        return query.toString();
    }

}
