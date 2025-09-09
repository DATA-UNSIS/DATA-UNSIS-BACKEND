package com.example.dynamicSQLTest.controllers;

import com.example.dynamicSQLTest.DTOs.request.GeneralQueryRequest;
import com.example.dynamicSQLTest.DTOs.request.QueryRequest;
import com.example.dynamicSQLTest.DTOs.response.FinalQueryResponse;
import com.example.dynamicSQLTest.services.DynamicQueryService;
import com.example.dynamicSQLTest.services.GeneralQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/data-unsis/api")
@Slf4j
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
            log.info("Dynamic query executed successfully. Returned {} results", results.size());
            log.debug("Dynamic query results: {}", results);
            return ResponseEntity.ok(results);
        }
        catch (Exception e) {
            log.error("Dynamic query execution failed: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("error", "Query execution failed", "message", e.getMessage()));
        }
    }
    @PostMapping("/execute-general-query")
    public ResponseEntity<FinalQueryResponse> executeQuerys(@RequestBody GeneralQueryRequest request){
        FinalQueryResponse response = generalQueryService.executeGeneralQuery(request);
        log.info("General query executed successfully. Response: {}", response);
        return ResponseEntity.ok(response);
    }
}
