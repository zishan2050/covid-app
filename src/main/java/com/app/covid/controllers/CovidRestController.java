package com.app.covid.controllers;

import com.app.covid.models.StateWiseCase;
import com.app.covid.models.User;
import com.app.covid.services.CovidDataService;
import com.app.covid.services.UserService;
import com.app.covid.validators.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/covid/api")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CovidRestController {

    private final UserService userService;
    private final CovidDataService covidDataService;
    private final UserValidator userValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(userValidator);
    }

    @PostMapping(path = "/sign_up", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<User> signUp(@Validated @RequestBody User user) {
        return userService.registerUser(user);
    }

    @GetMapping(path = "/data", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Flux<StateWiseCase> covidData() {
        return covidDataService.getCovidData();
    }

    @GetMapping(path = "/me", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<User> validateSession(@AuthenticationPrincipal Mono<UserDetails> userDetails) {
        return userService.retrieveUser(userDetails);
    }
}
