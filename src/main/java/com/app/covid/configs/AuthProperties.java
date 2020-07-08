package com.app.covid.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConfigurationProperties(prefix = "covid.auth")
@Configuration
@Data
public class AuthProperties {

    private String loginPageUrl;
    private String homePageUrl;
    private String errorPageUrl;
    private List<String> allowedCorsOrigin;
    private Long corsMaxAge;
}
