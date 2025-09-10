package com.example.dynamicSQLTest.DTOs.response;

import lombok.Data;

import java.util.List;

@Data
public class FinalQueryResponse {
    List<QueryResponse> results;
}
