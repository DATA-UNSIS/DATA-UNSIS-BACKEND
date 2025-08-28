package com.example.dynamicSQLTest.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dynamicSQLTest.DTOs.enums.ETitles;
import com.example.dynamicSQLTest.DTOs.request.GeneralQueryRequest;
import com.example.dynamicSQLTest.DTOs.response.QueryResponse;

import io.micrometer.common.lang.NonNull;

@Service
public class GeneralQueryService {

    @Autowired
    private QueryResponse results;
    
        public QueryResponse executeGeneralQuery(@NonNull GeneralQueryRequest request){
        String finalQuery;//Se ocupa para sacar los results
        //Map<String, Object> parameters = new HashMap<>();
        switch (request.getTitle()) {
            case ETitles.MAJOR_DISTRIBUTION :
                finalQuery = buildFinalQuery();
                break;
            case ETitles.ECONOMIC_LEVEL : 
                break;
            case ETitles.SCHOLARSHIPS_REQUESTED :
                break;
            case ETitles.HOUSEHOLD_SERVICES :
                break;
            case ETitles.CIVIL_STATE :
                break;
        }

        return results;
    }

    private String buildFinalQuery(){
        return "";
    }
}
