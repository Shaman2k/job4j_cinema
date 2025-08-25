package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

class SimpleUserServiceTest {
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    public void init() {
        userRepository = mock(UserRepository.class);
        userService = new SimpleUserService(userRepository);
    }

    @Test
    void whenSaveUserThenReturnUserOptional() {
        User user = new User(1, "user@user.com", "User", "123");
        when(userRepository.save(user)).thenReturn(Optional.of(user));

        Optional<User> result = userService.save(user);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("user@user.com");
    }

    @Test
    void whenFindByEmailAndPasswordAndSuccessThenReturnUserOptional() {
        User user = new User(1, "user@user.com", "User", "123");
        when(userRepository.findByEmailAndPassword("user@user.com", "123")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByEmailAndPassword("user@user.com", "123");

        assertThat(result).isPresent();
        assertThat(result.get().getFullName()).isEqualTo("User");
    }

    @Test
    void whenFindByEmailAndPasswordAndNotFoundThenReturnEmptyOptional() {
        when(userRepository.findByEmailAndPassword(any(String.class), any(String.class))).thenReturn(Optional.empty());

        Optional<User> result = userService.findByEmailAndPassword("user@user.com", "123");

        assertThat(result).isEmpty();
    }
}