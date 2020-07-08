package com.app.covid.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "covid.api")
@Configuration
@Data
public class CovidApiProperties {

    private String url;
}
