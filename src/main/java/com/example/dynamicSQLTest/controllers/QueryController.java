package com.example.dynamicSQLTest.controllers;

import com.example.dynamicSQLTest.DTOs.request.GeneralQueryRequest;
import com.example.dynamicSQLTest.DTOs.request.QueryRequest;
import com.example.dynamicSQLTest.services.DynamicQueryService;
import com.example.dynamicSQLTest.services.GeneralQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/data-unsis/api")
public class QueryController {

    @Autowired
    private DynamicQueryService dynamicQueryService;

    @Autowired
    private GeneralQueryService generalQueryService;

    QueryController(GeneralQueryService generalQueryService) {
        this.generalQueryService = generalQueryService;
    }

    @PostMapping("/execute-dynamic-query")
    public ResponseEntity<?> executeQuery(@RequestBody QueryRequest queryRequest) {
        try {
            List<Map<String, Object>> results = dynamicQueryService.executeDynamicQuery(queryRequest);
            return ResponseEntity.ok(results);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Query execution failed", "message", e.getMessage()));
        }
    }
    @PostMapping("/execute-general-query")
    public ResponseEntity<List<GeneralQueryRequest>> executeQuerys(@RequestBody GeneralQueryRequest request){
        return ResponseEntity.ok(generalQueryService.executeGeneralQuery(request));
    }
}
