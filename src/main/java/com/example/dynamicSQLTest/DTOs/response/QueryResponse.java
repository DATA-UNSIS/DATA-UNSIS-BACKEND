package com.example.dynamicSQLTest.DTOs.response;

import lombok.Data;

@Data
public class QueryResponse {
    private String title;
    private String[] major;
    private String[] semester;
    private String gender;
}
