package com.example.dynamicSQLTest.services;

import com.example.dynamicSQLTest.services.generalServices.CivilStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dynamicSQLTest.enums.ETitles;
import com.example.dynamicSQLTest.DTOs.request.GeneralQueryRequest;
import com.example.dynamicSQLTest.DTOs.response.QueryResponse;
import com.example.dynamicSQLTest.services.generalServices.HouseHoldServicesService;
import com.example.dynamicSQLTest.services.generalServices.ScholarShipsRequestedService;

import io.micrometer.common.lang.NonNull;

@Service
public class GeneralQueryService {

    @Autowired
    private ScholarShipsRequestedService scholarShipsRequestedService;
    @Autowired
    private CivilStateService civilStateService;

    @Autowired
    private HouseHoldServicesService houseHoldServicesService;

    private QueryResponse results;

        public QueryResponse executeGeneralQuery(@NonNull GeneralQueryRequest request){

        switch (request.getTitle()) {
            case ETitles.MAJOR_DISTRIBUTION :

                break;
            case ETitles.ECONOMIC_LEVEL : 
                break;
            case ETitles.SCHOLARSHIPS_REQUESTED :
                results = scholarShipsRequestedService.executeNativeQuery(ETitles.SCHOLARSHIPS_REQUESTED);
                break;
            case ETitles.HOUSEHOLD_SERVICES :
                results = houseHoldServicesService.executeNativeQuery(ETitles.HOUSEHOLD_SERVICES, request);
                break;
            case ETitles.CIVIL_STATE :
                results = civilStateService.executeNativeQuery(ETitles.CIVIL_STATE);
                break;
        }

        return results;
    }

}
