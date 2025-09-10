package com.example.dynamicSQLTest.DTOs.request;

import java.util.List;

import com.example.dynamicSQLTest.enums.ETitles;

import lombok.Data;
@Data
public class GeneralQueryRequest {
    private List<ETitles> titles;
    private List<String>  majors;
    private List<String> semesters;
    private String sexo;

}
