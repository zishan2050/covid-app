package com.app.covid.transformers;

import com.app.covid.models.User;
import com.app.covid.repositories.entities.UserEntity;

import java.util.function.Function;

public class UserToUserEntityTransformer implements Function<User, UserEntity> {

    @Override
    public UserEntity apply(User user) {
        return UserEntity.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
