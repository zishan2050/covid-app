package com.app.covid.services;

import com.app.covid.models.StateWiseCase;
import reactor.core.publisher.Flux;

public interface CovidDataService {

    Flux<StateWiseCase> getCovidData();
}
