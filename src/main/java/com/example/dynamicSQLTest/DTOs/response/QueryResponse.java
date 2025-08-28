package com.example.dynamicSQLTest.DTOs.response;

import com.example.dynamicSQLTest.enums.ETitles;
import com.example.dynamicSQLTest.DTOs.utils.DataDTO;

import lombok.Data;

@Data
public class QueryResponse {
    private ETitles title;
    private DataDTO data;
}
