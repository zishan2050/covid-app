package com.app.covid.services;

import com.app.covid.errorhandler.exceptions.CovidException;
import com.app.covid.models.User;
import com.app.covid.repositories.UserRepository;
import com.app.covid.repositories.entities.UserEntity;
import com.app.covid.services.impls.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;

public class UserServiceTest {

    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    private UserService userService;

    @BeforeEach
    public void setup() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void testRegisterUser() {
        User user = User.builder().firstName("John").lastName("Murphy").email("john@xyz.com").build();
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(UserEntity.builder().firstName("John").lastName("Murphy").email("john@xyz.com").build());
        User registeredUser = userService.registerUser(user).block();
        Assertions.assertTrue(registeredUser.getFirstName().equals(user.getFirstName()));
        Assertions.assertTrue(registeredUser.getLastName().equals(user.getLastName()));
        Assertions.assertTrue(registeredUser.getEmail().equals(user.getEmail()));
    }

    @Test
    public void testRegisterExistingUser() throws CovidException {
        User user = User.builder().firstName("John").lastName("Murphy").email("john@xyz.com").build();
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenThrow(new DataIntegrityViolationException("Unique key constraint violation"));
        Assertions.assertThrows(CovidException.class, () -> userService.registerUser(user).block());
    }
}
