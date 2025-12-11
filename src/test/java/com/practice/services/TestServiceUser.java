package com.practice.services;

import com.practice.models.User;
import com.practice.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestServiceTest { // Fixed class name

    @Mock
    private UserRepo mockTestRepo;

    @InjectMocks // Automatically injects mocks into TestService
    private UserService testServiceUnderUser;

    // Test constants
    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_ID = 1L;
    private static final Long NON_EXISTENT_ID = 999L;

    @BeforeEach
    void setUp() {
        // No need to manually create TestService - @InjectMocks handles it
        // But we can add any additional setup if needed
    }

    @Test
    void testSaveTest() {
        // Setup
        User inputUser = User.builder()
                .email(TEST_EMAIL)
                .build();

        User savedUser = User.builder()
                .id(TEST_ID)
                .email(TEST_EMAIL)
                .build();

        when(mockTestRepo.save(any(User.class))).thenReturn(savedUser);

        // Run the test
        final User result = testServiceUnderUser.saveTest(inputUser);

        // Verify the results
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(TEST_ID);
        assertThat(result.getEmail()).isEqualTo(TEST_EMAIL);
        verify(mockTestRepo).save(inputUser);
    }

    @Test
    void testExistByEmail_WhenEmailDoesNotExist() {
        // Setup
        when(mockTestRepo.existsByEmail(TEST_EMAIL)).thenReturn(false);

        // Run the test
        final boolean result = testServiceUnderUser.existByEmail(TEST_EMAIL);

        // Verify the results
        assertThat(result).isFalse();
        verify(mockTestRepo).existsByEmail(TEST_EMAIL);
    }

    @Test
    void testExistByEmail_WhenEmailExists() {
        // Setup
        when(mockTestRepo.existsByEmail(TEST_EMAIL)).thenReturn(true);

        // Run the test
        final boolean result = testServiceUnderUser.existByEmail(TEST_EMAIL);

        // Verify the results
        assertThat(result).isTrue();
        verify(mockTestRepo).existsByEmail(TEST_EMAIL);
    }

    @Test
    void testExistByEmail_WithNullEmail() {
        // Setup & Run the test
        // Note: Service implementation calls repo.existsByEmail(null) directly
        // So we need to mock what happens when null is passed
        when(mockTestRepo.existsByEmail(null)).thenReturn(false);

        final boolean result = testServiceUnderUser.existByEmail(null);

        // Verify the results
        assertThat(result).isFalse();
        verify(mockTestRepo).existsByEmail(null);
    }

    @Test
    void testGetAllTests_WhenTestsExist() {
        // Setup
        User user1 = User.builder().id(1L).build();
        User user2 = User.builder().id(2L).build();
        List<User> userList = List.of(user1, user2);

        when(mockTestRepo.findAll()).thenReturn(userList);

        // Run the test
        final List<User> result = testServiceUnderUser.getAllTests();

        // Verify the results
        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .containsExactly(user1, user2);
        verify(mockTestRepo).findAll();
    }

    @Test
    void testGetAllTests_WhenNoTestsExist() {
        // Setup
        when(mockTestRepo.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        final List<User> result = testServiceUnderUser.getAllTests();

        // Verify the results
        assertThat(result).isNotNull().isEmpty();
        verify(mockTestRepo).findAll();
    }

    @Test
    void testGetTestById_WhenTestExists() {
        // Setup
        User expectedUser = User.builder()
                .id(TEST_ID)
                .email(TEST_EMAIL)
                .build();

        when(mockTestRepo.findById(TEST_ID)).thenReturn(Optional.of(expectedUser));

        // Run the test
        final User result = testServiceUnderUser.getTestById(TEST_ID);

        // Verify the results
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(TEST_ID);
        assertThat(result.getEmail()).isEqualTo(TEST_EMAIL);
        verify(mockTestRepo).findById(TEST_ID);
    }

    @Test
    void testGetTestById_WhenTestDoesNotExist() {
        // Setup
        when(mockTestRepo.findById(NON_EXISTENT_ID)).thenReturn(Optional.empty());

        // Run the test
        final User result = testServiceUnderUser.getTestById(NON_EXISTENT_ID);

        // Verify the results
        assertThat(result).isNull();
        verify(mockTestRepo).findById(NON_EXISTENT_ID);
    }

    @Test
    void testGetTestById_WithNullId() {
        // Setup - Mock what happens when findById(null) is called
        when(mockTestRepo.findById(null)).thenReturn(Optional.empty());

        // Run the test
        final User result = testServiceUnderUser.getTestById(null);

        // Verify the results
        assertThat(result).isNull();
        verify(mockTestRepo).findById(null);
    }

    @Test
    void testDeleteTestById_Success() {
        // Setup
        User mockUser = User.builder()
                .id(TEST_ID)
                .email(TEST_EMAIL)
                .build();

        // Mock the findById call
        when(mockTestRepo.findById(TEST_ID)).thenReturn(Optional.of(mockUser));
        // No need to mock delete as it's void

        // Run the test
        testServiceUnderUser.deleteTestById(TEST_ID);

        // Verify the results
        verify(mockTestRepo).findById(TEST_ID);
        verify(mockTestRepo).delete(mockUser); // Important: delete(user) not deleteById(id)
    }

    @Test
    void testDeleteTestById_WhenUserNotFound() {
        // Setup - Mock findById to return empty (user not found)
        when(mockTestRepo.findById(NON_EXISTENT_ID)).thenReturn(Optional.empty());

        // Run the test and verify exception is thrown
        assertThatThrownBy(() -> testServiceUnderUser.deleteTestById(NON_EXISTENT_ID))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");

        // Verify the results
        verify(mockTestRepo).findById(NON_EXISTENT_ID);
        verify(mockTestRepo, never()).delete(any(User.class));
    }

    @Test
    void testDeleteTestById_WithNullId() {

        when(mockTestRepo.findById(NON_EXISTENT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> testServiceUnderUser.deleteTestById(NON_EXISTENT_ID))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");

        verify(mockTestRepo).findById(NON_EXISTENT_ID);
        verify(mockTestRepo, never()).delete(any(User.class));
    }

    @Test
    void updateUserById_ifUserExist() {
        // Arrange
        User existingUser = User.builder()
                .id(TEST_ID)
                .email(TEST_EMAIL)
                .name("Original Name")
                .build();

        User updateRequest = User.builder()
                .name("Updated Name")
                .build();

        User expectedUpdatedUser = User.builder()
                .id(TEST_ID)
                .email(TEST_EMAIL) // Email should remain unchanged
                .name("Updated Name")
                .build();

        when(mockTestRepo.findById(TEST_ID)).thenReturn(Optional.of(existingUser));
        when(mockTestRepo.save(any(User.class))).thenReturn(expectedUpdatedUser);

        // Act
        final User result = testServiceUnderUser.update(TEST_ID, updateRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(TEST_ID);
        assertThat(result.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(result.getName()).isEqualTo("Updated Name");

        // Verify interactions
        verify(mockTestRepo).findById(TEST_ID);
        verify(mockTestRepo).save(any(User.class));
    }
}