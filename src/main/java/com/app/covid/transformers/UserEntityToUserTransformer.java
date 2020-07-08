package com.app.covid.transformers;

import com.app.covid.models.User;
import com.app.covid.repositories.entities.UserEntity;

import java.util.function.Function;

public class UserEntityToUserTransformer implements Function<UserEntity, User> {

    @Override
    public User apply(UserEntity userEntity) {
        return User.builder()
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .createdAt(userEntity.getCreatedAt())
                .build();
    }
}
