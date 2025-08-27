package com.example.dynamicSQLTest.DTOs.utils;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class DataDTO {
    private Map<String, Object> data = new HashMap<>();
}
