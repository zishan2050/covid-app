package com.app.covid.repositories;

import com.app.covid.repositories.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    public UserEntity findByEmail(String email);
}
