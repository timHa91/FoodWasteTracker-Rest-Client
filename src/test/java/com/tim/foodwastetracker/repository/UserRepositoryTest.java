package com.tim.foodwastetracker.repository;

import com.tim.foodwastetracker.model.User;
import com.tim.foodwastetracker.model.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @Test
    void itShouldFindUserByEmail() {
        // given
        String email = "test@test.com";
        User user = new User(
                1L,
                "test",
                email,
                "Testfirst",
                "Testlast",
                LocalDate.now(),
                LocalDate.now(),
                true,
                UserRole.USER);
        underTest.save(user);
        // when
        Optional<User> expectedUser = underTest.findByEmail(email);
        // then
        expectedUser.ifPresent(value -> assertThat(value).isEqualTo(user));
    }

    @Test
    void itShouldNotFindUserByEmail() {
        // given
        String email = "test@test.com";
        String notExistentEmail = "test1@test.com";
        User user = new User(
                1L,
                "testPassword",
                email,
                "TestFirst",
                "TestLast",
                LocalDate.now(),
                LocalDate.now(),
                true,
                UserRole.USER);
        underTest.save(user);
        // when
        Optional<User> expectedUser = underTest.findByEmail(notExistentEmail);
        // then
        assertThat(expectedUser).isEmpty();
    }
}