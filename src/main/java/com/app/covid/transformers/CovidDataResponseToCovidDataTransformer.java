package com.app.covid.transformers;

import com.app.covid.models.CovidData;
import com.app.covid.models.CovidDataResponse;
import com.app.covid.models.StateWiseCase;

import java.time.ZoneId;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CovidDataResponseToCovidDataTransformer implements Function<CovidDataResponse, CovidData> {

    @Override
    public CovidData apply(CovidDataResponse covidDataResponse) {
        return CovidData.builder()
                .stateWiseCases(covidDataResponse.getStatewise().parallelStream().map(statewiseCaseCountResponse -> {
                    return StateWiseCase.builder()
                            .active(Long.parseLong(statewiseCaseCountResponse.getActive()))
                            .confirmed(Long.parseLong(statewiseCaseCountResponse.getConfirmed()))
                            .deaths(Long.parseLong(statewiseCaseCountResponse.getDeaths()))
                            .recovered(Long.parseLong(statewiseCaseCountResponse.getRecovered()))
                            .state(statewiseCaseCountResponse.getState())
                            .stateCode(statewiseCaseCountResponse.getStatecode())
                            .stateNotes(statewiseCaseCountResponse.getStatenotes())
                            .deltaConfirmed(Long.parseLong(statewiseCaseCountResponse.getDeltaconfirmed()))
                            .deltaDeaths(Long.parseLong(statewiseCaseCountResponse.getDeltadeaths()))
                            .deltaRecovered(Long.parseLong(statewiseCaseCountResponse.getDeltarecovered()))
                            .migratedOther(Long.parseLong(statewiseCaseCountResponse.getMigratedother()))
                            .lastUpdatedTime(statewiseCaseCountResponse.getLastupdatedtime().atZone(ZoneId.of("Asia/Kolkata")).toOffsetDateTime())
                            .build();
                }).collect(Collectors.toList()))
                .build();
    }
}
