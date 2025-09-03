package com.example.dynamicSQLTest.services;

import com.example.dynamicSQLTest.DTOs.response.FinalQueryResponse;
import com.example.dynamicSQLTest.common.GeneralQuerysConstants;
import com.example.dynamicSQLTest.enums.EHouseholdServices;
import com.example.dynamicSQLTest.enums.EScholarships;
import com.example.dynamicSQLTest.processors.TitlesLogicProcessor;
import com.example.dynamicSQLTest.services.generalServices.EconomicLevelService;
import com.example.dynamicSQLTest.services.generalServices.CivilStateService;
import com.example.dynamicSQLTest.services.generalServices.InstitutionOriginService;
import com.example.dynamicSQLTest.services.generalServices.MajorDistributionService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dynamicSQLTest.enums.ETitles;
import com.example.dynamicSQLTest.DTOs.request.GeneralQueryRequest;
import com.example.dynamicSQLTest.DTOs.response.QueryResponse;

import io.micrometer.common.lang.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Data
public class GeneralQueryService {

    @Autowired
    private CivilStateService civilStateService;
    @Autowired
    MajorDistributionService majorDistributionService;
    @Autowired
    EconomicLevelService economicLevelService;
    @Autowired
    InstitutionOriginService institutionOriginService;
    @Autowired
    private TitlesLogicProcessor titlesLogicProcessor;

    private QueryResponse results;
    List<QueryResponse> allResults = new ArrayList<>();
    public FinalQueryResponse executeGeneralQuery(@NonNull GeneralQueryRequest request){
            allResults.clear();
            List<String> tables = new ArrayList<>();
            List<ETitles> titles = request.getTitles();
            for (ETitles title : titles) {
                switch (title) {
                    case ETitles.MAJOR_DISTRIBUTION:
                        results = majorDistributionService.executeMajorDistributionQuery(request);
                        allResults.add(results);
                        break;
                    case ETitles.ECONOMIC_LEVEL:
                        results = economicLevelService.executeEconomicLevelQuery(request);
                        allResults.add(results);
                        break;
                    case ETitles.SCHOLARSHIPS_REQUESTED:
                        tables = new ArrayList<>(Arrays.asList("becas", "alumnos"));
                        results = titlesLogicProcessor.executeNativeQuery(ETitles.SCHOLARSHIPS_REQUESTED, request, tables,
                                GeneralQuerysConstants.COUNT_SCHOLARSHIPS_REJECTED,
                                GeneralQuerysConstants.FILTERS_COUNT_SCHOLARSHIPS_REJECTED, EScholarships.class);
                        tables.clear();
                        allResults.add(results);
                        break;
                    case ETitles.HOUSEHOLD_SERVICES:
                        tables = new ArrayList<>(Arrays.asList("servicios", "alumnos"));
                        results = titlesLogicProcessor.executeNativeQuery(ETitles.HOUSEHOLD_SERVICES, request, tables,
                                GeneralQuerysConstants.COUNT_HOUSE_HOULD_SERVICES,
                                GeneralQuerysConstants.FILTERS_COUNT_HOUSE_HOULD_SERVICES, EHouseholdServices.class);
                        tables.clear();
                        allResults.add(results);
                        break;
                    case ETitles.CIVIL_STATE:
                        results = civilStateService.executeNativeQuery(ETitles.CIVIL_STATE, request);
                        allResults.add(results);
                        break;
                    case ETitles.TYPE_INSTITUTION_PROCEDENCY:
                        results = institutionOriginService.executeNativeQuery(ETitles.TYPE_INSTITUTION_PROCEDENCY, request);
                        allResults.add(results);
                        break;
                }
            }
        FinalQueryResponse finalResults = new FinalQueryResponse();
        finalResults.setResults(allResults);
        return finalResults;
    }

}
