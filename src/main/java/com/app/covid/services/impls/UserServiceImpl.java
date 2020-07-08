package com.app.covid.services.impls;

import com.app.covid.errorhandler.exceptions.CovidException;
import com.app.covid.models.User;
import com.app.covid.repositories.UserRepository;
import com.app.covid.services.UserService;
import com.app.covid.transformers.UserEntityToUserTransformer;
import com.app.covid.transformers.UserToUserEntityTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Mono<User> registerUser(User user) {
        try {
            return Mono.just(new UserEntityToUserTransformer().apply(userRepository.save(new UserToUserEntityTransformer().apply(user))));
        } catch (DataIntegrityViolationException e) {
            throw new CovidException(HttpStatus.CONFLICT, "User already exist, please try again with different email", Collections.singletonList("User already exist"));
        } catch (Exception e) {
            log.info("Exception registering user", e);
            throw new CovidException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong, please try again", e);
        }
    }

    @Override
    public Mono<User> retrieveUser(Mono<UserDetails> userDetails) {
        try {
            return userDetails.filter(userDetail -> StringUtils.isNotBlank(userDetail.getUsername())).hasElement().flatMap(hasElement -> {
                if (hasElement) {
                    return userDetails.map(user -> new UserEntityToUserTransformer().apply(userRepository.findByEmail(user.getUsername())));
                } else {
                    return Mono.error(new CovidException(HttpStatus.UNAUTHORIZED, "Invalid session id", Collections.emptyList()));
                }
            });
        } catch (Exception e) {
            log.info("Exception retrieving user", e);
            throw new CovidException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong, please try again", e);
        }
    }
}
