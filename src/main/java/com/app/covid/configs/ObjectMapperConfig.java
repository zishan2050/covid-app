package com.app.covid.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
@ConditionalOnProperty(matchIfMissing = true, prefix = "object-mapper", name = "snake-case")
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
        return jackson2ObjectMapperBuilder.propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE).build();
    }
}
