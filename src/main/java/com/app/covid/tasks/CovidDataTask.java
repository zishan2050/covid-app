package com.app.covid.tasks;

import com.app.covid.configs.CovidApiProperties;
import com.app.covid.constants.CovidAppConstant;
import com.app.covid.models.CovidDataResponse;
import com.app.covid.transformers.CovidDataResponseToCovidDataTransformer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.LocalDateTime;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CovidDataTask {

    private final ReactiveRedisOperations<String, String> redisOperations;
    private final ObjectMapper objectMapper;
    private final CovidApiProperties covidApiProperties;

    @Scheduled(cron = "${covid.task.cron}", zone = "${covid.task.zone}")
    public void refreshCovidData() {
        try {
            log.info("Refreshing covid data");

            ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                    .codecs(configurer -> {
                        configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));
                        configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper));
                    })
                    .build();
            WebClient.builder()
                    .clientConnector(new ReactorClientHttpConnector(
                            HttpClient.create().wiretap(true)
                    ))
                    .exchangeStrategies(exchangeStrategies)
                    .baseUrl(covidApiProperties.getUrl())
                    .build()
                    .get()
                    .retrieve()
                    .bodyToMono(CovidDataResponse.class)
                    .map(covidDataResponse -> {
                        try {
                            return objectMapper.writeValueAsString(new CovidDataResponseToCovidDataTransformer().apply(covidDataResponse));
                        } catch (JsonProcessingException e) {
                            log.error("Failed to convert covid data to string", e);
                            return null;
                        }
                    })
                    .subscribe(covidData -> {
                        redisOperations.opsForValue().set(CovidAppConstant.COVID_DATA_REDIS_KEY, covidData)
                                .subscribe(updateResponse -> log.info("Covid data refreshed at {}", LocalDateTime.now()));
                    });
        } catch (Exception e) {
            log.error("Exception refreshing covid data", e);
        }
    }
}
