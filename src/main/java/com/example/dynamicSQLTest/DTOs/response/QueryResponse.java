package com.example.dynamicSQLTest.DTOs.response;

import com.example.dynamicSQLTest.enums.ETitles;

import java.util.HashMap;
import java.util.Map;


import lombok.Data;

@Data
public class QueryResponse {
    private ETitles title;
    private Map<String, Object> data = new HashMap<>();
}
