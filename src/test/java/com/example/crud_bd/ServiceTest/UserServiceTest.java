package com.example.crud_bd.ServiceTest;

import com.example.crud_bd.Entity.User;
import com.example.crud_bd.Exceptions.UserNotFoundException;
import com.example.crud_bd.Repository.UserRepository;
import com.example.crud_bd.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
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

    private User user;
    private List<User> users;

    @BeforeEach
    void setUp() {
        user = getUser();
        users = new ArrayList<>();
        users.add(user);
    }

    @Test
    public void testGetUserById_shouldReturnUserWhenValidId() {
        when(userRepository.getUserById(1L)).thenReturn(user);
        User result = userService.getUserById(1L);
        assertEquals(20, result.getAge());
    }

    @Test
    public void testGetUserById_shouldReturnThrowWhenInvalidId() {
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getUserById(null));
        assertEquals(IDCANNOTBEEMPTY, exception.getMessage());
    }

    @Test
    public void testGetUserById_shouldReturnThrowWhenUserNull() {
        when(userRepository.getUserById(1L)).thenReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
        assertEquals(USERNOTFOUND, exception.getMessage());
    }

    @Test
    public void testGetAllUsers_shouldReturnUsersList() {
        when(userRepository.findAll()).thenReturn(users);
        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
    }

    @Test
    public void testGetAllUsers_shouldReturnThrowWhenListEmpty() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getAllUsers());
        assertEquals(USERSNOTFOUND, exception.getMessage());
    }

    @Test
    public void testGetAllAdultUsers_shouldReturnNoAdultUsersList(){
        User adultUser = users.get(0);
        adultUser.setAge(14);
        users.add(adultUser);
        users.remove(0);

        when(userRepository.findAll()).thenReturn(users);

        assertFalse(userService.getAllUsers().get(0).getAge() >= 18);
    }

    @Test
    public void testGetAllAdultUsers_shouldReturnAdultUsersList(){
        when(userRepository.findAll()).thenReturn(users);

        assertTrue(userService.getAllUsers().get(0).getAge() >= 18);
    }

    @Test
    public void testCreateUser_shouldCreateUser() {
        when(userRepository.save(user)).thenReturn(user);
        User result = userService.createUser(user);

        assertEquals(user, result);
    }

    @Test
    public void testCreateUser_shouldThrowWhenUserNull(){
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.createUser(null));
        assertEquals(USERCANNOTBEEMPTY, exception.getMessage());
    }

    @Test
    public void testCreateUser_shouldThrowWhenUserAlreadyExists(){
        when(userRepository.save(user)).thenReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.createUser(user));
        assertEquals(USERNOTFOUND, exception.getMessage());
    }

    @DisplayName("проверяет удаление")
    @Test
    public void testDeleteUserById_shouldDeleteUser() {
       when(userRepository.deleteUserById(1L)).thenReturn(1);
       assertEquals(HttpStatus.OK, userService.deleteUserById(1L));
    }

    @Test
    public void testDeleteUserById_shouldThrowWhenIdEmpty() {
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(null));
        assertEquals(IDCANNOTBEEMPTY, exception.getMessage());
    }

    @Test
    public void testDeleteUserById_ShouldThrowWhenUserNotFound() {
        when(userRepository.deleteUserById(1L)).thenReturn(0);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(1L));
        assertEquals(USERNOTFOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testDeleteUserByPassport_shouldDeleteUserByPassport() {
        when(userRepository.deleteUserByPassport("4321-543552")).thenReturn(1);
        assertEquals(HttpStatus.OK, userService.deleteUserByPassport("4321-543552"));
    }

    @Test
    public void testDeleteUserByPassport_shouldThrowWhenUserNotFound() {

    }

    public User getUser() {
        return User.builder()
                .id(1L)
                .firstName("bob")
                .secondName("Lol")
                .age(20)
                .passport("4321-543552")
                .build();
    }
}
