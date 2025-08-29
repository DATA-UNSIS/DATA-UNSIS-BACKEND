package com.example.dynamicSQLTest.services;

import com.example.dynamicSQLTest.services.generalServices.MajorDistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dynamicSQLTest.enums.ETitles;
import com.example.dynamicSQLTest.DTOs.request.GeneralQueryRequest;
import com.example.dynamicSQLTest.DTOs.response.QueryResponse;
import com.example.dynamicSQLTest.services.generalServices.ScholarShipsRequestedService;

import io.micrometer.common.lang.NonNull;

@Service
public class GeneralQueryService {

    @Autowired
    private ScholarShipsRequestedService scholarShipsRequestedService;
    @Autowired
    MajorDistributionService majorDistributionService;

    private QueryResponse results;

        public QueryResponse executeGeneralQuery(@NonNull GeneralQueryRequest request){

        switch (request.getTitle()) {
            case ETitles.MAJOR_DISTRIBUTION :
                results = majorDistributionService.executeMajorDistributionQuery(request);
                break;
            case ETitles.ECONOMIC_LEVEL : 
                break;
            case ETitles.SCHOLARSHIPS_REQUESTED :
                results = scholarShipsRequestedService.executeNativeQuery(ETitles.SCHOLARSHIPS_REQUESTED);
                break;
            case ETitles.HOUSEHOLD_SERVICES :
                break;
            case ETitles.CIVIL_STATE :
                break;
        }

        return results;
    }

}
