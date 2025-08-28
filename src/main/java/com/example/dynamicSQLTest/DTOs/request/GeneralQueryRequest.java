package com.example.dynamicSQLTest.DTOs.request;

import com.example.dynamicSQLTest.DTOs.utils.MajorDTO;
import com.example.dynamicSQLTest.DTOs.utils.SemesterDTO;
import com.example.dynamicSQLTest.enums.ETitles;

import lombok.Data;
@Data
public class GeneralQueryRequest {
    private ETitles title;
    private MajorDTO major;
    private SemesterDTO semester;
    private String sexo;

}
