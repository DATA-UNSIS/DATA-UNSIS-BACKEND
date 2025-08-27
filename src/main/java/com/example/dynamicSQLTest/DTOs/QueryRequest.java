package com.example.dynamicSQLTest.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class QueryRequest {
    private String baseQuery;
    private Map<String, Object> filters;
    private List<String> groupBy;
    private List<String> orderBy;
    private Integer limit;
    private List<String> selectFields;
    private List<String> fromTables;
    private Map<String, Object> havingConditions;
}