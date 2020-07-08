package com.app.covid.services.impls;

import com.app.covid.constants.CovidAppConstant;
import com.app.covid.models.CovidData;
import com.app.covid.models.StateWiseCase;
import com.app.covid.services.CovidDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CovidDataServiceImpl implements CovidDataService {

    private final ReactiveRedisOperations<String, String> redisOperations;
    private final ObjectMapper objectMapper;

    @Override
    public Flux<StateWiseCase> getCovidData() {
        return redisOperations.opsForValue().get(CovidAppConstant.COVID_DATA_REDIS_KEY).map(covidData -> {
            try {
                return objectMapper.readValue(covidData, CovidData.class).getStateWiseCases();
            } catch (JsonProcessingException e) {
                log.error("Failed to parse covid data", e);
                return Collections.singletonList(StateWiseCase.builder().build());
            }
        }).flatMapMany(Flux::fromIterable);
    }
}
