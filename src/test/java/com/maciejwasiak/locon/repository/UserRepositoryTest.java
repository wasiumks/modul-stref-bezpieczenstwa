package com.maciejwasiak.locon.repository;

import com.maciejwasiak.locon.model.User;
import com.maciejwasiak.locon.model.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByPhone_WhenUserExists_ShouldReturnUser() {
        // Given
        String phone = "+48123456789";
        User user = new User(phone, UserRole.ADMIN);
        entityManager.persistAndFlush(user);

        // When
        Optional<User> foundUser = userRepository.findByPhone(phone);

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(phone, foundUser.get().getPhone());
        assertEquals(UserRole.ADMIN, foundUser.get().getRole());
    }

    @Test
    void testFindByPhone_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // Given
        String phone = "+48999999999";

        // When
        Optional<User> foundUser = userRepository.findByPhone(phone);

        // Then
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testFindByPhone_WithDifferentPhones_ShouldReturnCorrectUser() {
        // Given
        User user1 = new User("+48123456789", UserRole.ADMIN);
        User user2 = new User("+48987654321", UserRole.USER);
        User user3 = new User("+48555666777", UserRole.VIEWER);
        
        entityManager.persistAndFlush(user1);
        entityManager.persistAndFlush(user2);
        entityManager.persistAndFlush(user3);

        // When & Then
        Optional<User> foundUser1 = userRepository.findByPhone("+48123456789");
        assertTrue(foundUser1.isPresent());
        assertEquals(UserRole.ADMIN, foundUser1.get().getRole());

        Optional<User> foundUser2 = userRepository.findByPhone("+48987654321");
        assertTrue(foundUser2.isPresent());
        assertEquals(UserRole.USER, foundUser2.get().getRole());

        Optional<User> foundUser3 = userRepository.findByPhone("+48555666777");
        assertTrue(foundUser3.isPresent());
        assertEquals(UserRole.VIEWER, foundUser3.get().getRole());
    }

    @Test
    void testSaveUser_ShouldPersistCorrectly() {
        // Given
        User user = new User("+48123456789", UserRole.ADMIN);
        user.setOtpCode("123456");

        // When
        User savedUser = userRepository.save(user);

        // Then
        assertNotNull(savedUser.getId());
        assertEquals("+48123456789", savedUser.getPhone());
        assertEquals(UserRole.ADMIN, savedUser.getRole());
        assertEquals("123456", savedUser.getOtpCode());
    }

    @Test
    void testUpdateUser_ShouldUpdateCorrectly() {
        // Given
        User user = new User("+48123456789", UserRole.ADMIN);
        User savedUser = userRepository.save(user);
        
        // When
        savedUser.setRole(UserRole.USER);
        savedUser.setOtpCode("654321");
        User updatedUser = userRepository.save(savedUser);

        // Then
        assertEquals(savedUser.getId(), updatedUser.getId());
        assertEquals(UserRole.USER, updatedUser.getRole());
        assertEquals("654321", updatedUser.getOtpCode());
    }
}
