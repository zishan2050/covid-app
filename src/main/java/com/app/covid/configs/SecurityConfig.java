package com.app.covid.configs;

import com.app.covid.services.impls.ReactiveUserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.HttpStatusReturningServerLogoutSuccessHandler;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;
import org.springframework.web.server.WebFilter;

@Configuration
@EnableWebFluxSecurity
@EnableRedisWebSession
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class SecurityConfig {

    private final AuthProperties authProperties;
    private final ReactiveUserDetailsServiceImpl reactiveUserDetailsService;

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) throws Exception {
        return http.authorizeExchange()
                .pathMatchers("/covid/api/actuator/**", "/covid/api/sign_up", "/public/**", "/covid/app/**")
                .permitAll()
                .anyExchange().authenticated()
                .and()
                .formLogin()
                .loginPage("/covid/api/login")
                .authenticationManager(new UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService))
                .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler(authProperties.getHomePageUrl()))
                .authenticationFailureHandler(new RedirectServerAuthenticationFailureHandler(authProperties.getErrorPageUrl()))
                .and()
                .logout()
                .logoutUrl("/covid/api/logout")
                .logoutSuccessHandler(new HttpStatusReturningServerLogoutSuccessHandler(HttpStatus.OK))
                .and()
                .addFilterAfter(corsWebFilter(), SecurityWebFiltersOrder.LAST)
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .csrf().disable()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    private void addCorsHeader(String allowedOrigin, HttpHeaders httpHeaders) {
        httpHeaders.add("Access-Control-Allow-Origin", allowedOrigin);
        httpHeaders.add("Access-Control-Allow-Methods", "*");
        httpHeaders.add("Access-Control-Max-Age", authProperties.getCorsMaxAge().toString());
        httpHeaders.add("Access-Control-Allow-Headers", "*");
        httpHeaders.add("Access-Control-Allow-Credentials", "true");
    }

    public WebFilter corsWebFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            String requestOrigin = request.getHeaders().getOrigin();
            String allowedOrigin = authProperties.getAllowedCorsOrigin().contains(requestOrigin) ? requestOrigin : StringUtils.join(authProperties.getAllowedCorsOrigin(), ',');
            HttpHeaders httpHeaders = response.getHeaders();
            addCorsHeader(allowedOrigin, httpHeaders);
            return chain.filter(exchange);
        };
    }
}
