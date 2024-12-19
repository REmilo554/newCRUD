package com.example.crud_bd.ServiceTest;

import com.example.crud_bd.Entity.User;
import com.example.crud_bd.Exceptions.UserNotFoundException;
import com.example.crud_bd.Repository.UserRepository;
import com.example.crud_bd.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private final String USERNOTFOUND = "User not found";
    private final String USERSNOTFOUND = "Users not found";
    private final String IDCANNOTBEEMPTY = "Id cannot be empty";


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
    public void testGetAllUsers_shouldReturnUsersListWhenNull() {
        when(userRepository.findAll()).thenReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getAllUsers());
        assertEquals(USERSNOTFOUND, exception.getMessage());
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
