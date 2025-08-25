package com.example.dynamicSQLTest.controllers;

import com.example.dynamicSQLTest.DTOs.QueryRequest;
import com.example.dynamicSQLTest.services.DynamicQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/query")
public class QueryController {
    @Autowired
    private DynamicQueryService dynamicQueryService;

    @PostMapping("/execute")
    public ResponseEntity<?> executeQuery(@RequestBody QueryRequest queryRequest) {
        try {
            List<Map<String, Object>> results = dynamicQueryService.executeDynamicQuery(queryRequest);
            return ResponseEntity.ok(results);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Query execution failed", "message", e.getMessage()));
        }
    }
}
