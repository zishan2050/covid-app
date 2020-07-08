package com.app.covid.services;

import com.app.covid.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<User> registerUser(User user);

    Mono<User> retrieveUser(Mono<UserDetails> userDetails);
}
