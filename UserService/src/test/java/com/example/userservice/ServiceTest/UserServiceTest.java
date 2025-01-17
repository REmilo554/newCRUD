package com.example.userservice.ServiceTest;


import com.example.userservice.Entity.User;
import com.example.userservice.Exceptions.UserNotFoundException;
import com.example.userservice.Kafka.KafkaProducerService;
import com.example.userservice.Repository.UserRepository;
import com.example.userservice.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private final String USERNOTFOUND = "User not found";
    private final String USERSNOTFOUND = "Users not found";
    private final String IDCANNOTBEEMPTY = "Id cannot be empty";
    private final String USERCANNOTBEEMPTY = "User cannot be empty";

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    @Mock
    KafkaProducerService kafkaProducerService;

    HttpStatus status;

    private User user;
    private List<User> users;
    private Map<String, Object> usersMap;

    @BeforeEach
    void setUp() {
        user = getUser();
        users = new ArrayList<>();
        usersMap = new HashMap<>();
        users.add(user);
        usersMap.put("firstName", user.getFirstName());
        usersMap.put("secondName", user.getSecondName());
        usersMap.put("age", user.getAge());
        usersMap.put("passport", user.getPassport());
    }

    @Test
    public void testFindUserById_shouldReturnUserWhenValidId() {
        when(userRepository.findUserById(1L)).thenReturn(user);
        User result = userService.findUserById(1L);
        assertEquals(14, result.getAge());
    }

    @Test
    public void testFindUserById_shouldReturnThrowWhenInvalidId() {
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.findUserById(null));
        assertEquals(IDCANNOTBEEMPTY, exception.getMessage());
    }

    @Test
    public void testFindUserById_shouldReturnThrowWhenUserNull() {
        when(userRepository.findUserById(1L)).thenReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.findUserById(1L));
        assertEquals(USERNOTFOUND, exception.getMessage());
    }

    @Test
    public void testFindAllUsers_shouldReturnUsersList() {
        when(userRepository.findAll()).thenReturn(users);
        List<User> result = userService.findAll();

        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
    }

    @Test
    public void testFindAllUsers_shouldReturnThrowWhenListEmpty() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.findAll());
        assertEquals(USERSNOTFOUND, exception.getMessage());
    }

    @Test
    public void testFindAllAdultUsers_shouldReturnNoAdultUsersList() {
        when(userRepository.findAllAdultUsers()).thenReturn(users);

        assertTrue(userService.findAllAdultUsers().get(0).getAge() < 18);
    }

    @Test
    public void testFindAllAdultUsers_shouldReturnAdultUsersList() {
        when(userRepository.findAllAdultUsers()).thenReturn(users);

        assertFalse(userService.findAllAdultUsers().get(0).getAge() >= 18);
    }

    @Test
    public void testSaveUser_shouldSaveUser() {
        when(userRepository.save(user)).thenReturn(user);
        User result = userService.save(user);

        assertEquals(user, result);
    }

    @Test
    public void testSaveUser_shouldThrowWhenUserNull() {
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.save(null));
        assertEquals(USERCANNOTBEEMPTY, exception.getMessage());
    }

    @Test
    public void testSaveUser_shouldThrowWhenUserAlreadyExists() {
        when(userRepository.save(user)).thenReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.save(user));
        assertEquals(USERNOTFOUND, exception.getMessage());
    }


    @DisplayName("проверяет удаление")
    @Test
    public void testDeleteUserById_shouldDeleteUser() {
        when(userRepository.deleteUserById(1L)).thenReturn(user);
        User result = userService.deleteUserById(1L);
        assertEquals(user, result);
    }

    @Test
    public void testDeleteUserById_shouldThrowWhenIdEmpty() {
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(null));
        assertEquals(IDCANNOTBEEMPTY, exception.getMessage());
    }

    @Test
    public void testDeleteUserById_ShouldThrowWhenUserNotFound() {
        when(userRepository.deleteUserById(1L)).thenReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(1L));
        assertEquals(USERNOTFOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testDeleteUserByPassport_shouldDeleteUserByPassport() {
        when(userRepository.deleteUserByPassport("4321-543552")).thenReturn(user);

        assertEquals(user, userService.deleteUserByPassport("4321-543552"));
    }

    @Test
    public void testDeleteUserByPassport_shouldThrowWhenPassportIsNull() {
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.deleteUserByPassport(null));
        assertEquals("Passport cannot be null", exception.getMessage());
    }

    @Test
    public void testDeleteUserByPassport_shouldThrowWhenUserNotFound() {
        when(userRepository.deleteUserByPassport("4321-543552")).thenReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.deleteUserByPassport("4321-543552"));

        assertEquals(USERNOTFOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testUpdateFullUser_shouldUpdateUser() {
        when(userRepository.updateUser(user.getId(), user.getFirstName(),
                user.getSecondName(), user.getAge(), user.getPassport())).thenReturn(user);
        User result = userService.updateUser(user);

        assertEquals(user, result);
    }

    @Test
    public void testUpdateFullUser_shouldThrowWhenUserIsNull() {
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.updateUser(null));

        assertEquals("User not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testUpdateFullUser_shouldThrowWhenUserNotUpdated() {

        when(userRepository.updateUser(1L, user.getFirstName(),
                user.getSecondName(), user.getAge(), user.getPassport())).thenReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.updateUser(user));

        assertEquals("User not updated", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testUpdateUserById_shouldUpdateUser() {
        when(userRepository.findUserById(1L)).thenReturn(user);
        when(userRepository.updateUserById(1L,
                user.getFirstName(), user.getSecondName(),
                user.getAge(), user.getPassport()))
                .thenReturn(user);

        assertEquals(user, userService.findUserById(1L));
        assertEquals(user, userService.updateUserById(1L, usersMap));
    }


    @Test
    public void testUpdateUserById_shouldThrowWhenUserNotFound() {
        when(userRepository.findUserById(1L)).thenReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.findUserById(1L));

        assertEquals(USERNOTFOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testUpdateUserById_shouldThrowWhenUserNotUpdated() {
        when(userRepository.findUserById(1L)).thenReturn(user);
        when(userRepository.updateUserById(1L, user.getFirstName()
                , user.getSecondName(), user.getAge(), user.getPassport())).thenReturn(null);

        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> userService.updateUserById(1L, usersMap));

        assertEquals(user, userService.findUserById(1L));
        assertEquals("User not updated", userNotFoundException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, userNotFoundException.getStatus());
    }


    public User getUser() {
        return User.builder()
                .id(1L)
                .firstName("bob")
                .secondName("Lol")
                .age(14)
                .passport("4321-543552")
                .build();
    }
}
