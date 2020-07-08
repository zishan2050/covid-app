package com.app.covid.services.impls;

import com.app.covid.repositories.UserRepository;
import com.app.covid.repositories.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        UserEntity userEntity = userRepository.findByEmail(username);
        if (Objects.isNull(userEntity)) {
            throw new UsernameNotFoundException("User not found");
        }
        return Mono.just(new org.springframework.security.core.userdetails.User(userEntity.getEmail(), userEntity.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("USER"))));
    }
}
