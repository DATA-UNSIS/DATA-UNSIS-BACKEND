package com.example.dynamicSQLTest.DTOs.response;

import com.example.dynamicSQLTest.DTOs.utils.DataDTO;

import lombok.Data;

@Data
public class QueryResponse {
    private String title;
    private DataDTO data;
}
