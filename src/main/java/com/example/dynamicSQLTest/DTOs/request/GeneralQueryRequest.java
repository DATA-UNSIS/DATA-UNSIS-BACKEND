package com.example.dynamicSQLTest.DTOs.request;

import com.example.dynamicSQLTest.DTOs.enums.ETitles;
import com.example.dynamicSQLTest.DTOs.utils.MajorDTO;
import com.example.dynamicSQLTest.DTOs.utils.SemesterDTO;

import lombok.Data;
@Data
public class GeneralQueryRequest {
    private ETitles title;
    private MajorDTO major;
    private SemesterDTO semester;
    private String sexo;

}
