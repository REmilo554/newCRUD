package com.example.crud_bd.ServiceTest;

import com.example.crud_bd.Entity.User;
import com.example.crud_bd.Exceptions.UserNotFoundException;
import com.example.crud_bd.Kafka.KafkaProducerService;
import com.example.crud_bd.Repository.UserRepository;
import com.example.crud_bd.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

    private User user;
    private List<User> users;
    private Map<String,Object> usersMap;

    @BeforeEach
    void setUp() {
        user = getUser();
        users = new ArrayList<>();
        usersMap = new HashMap<>();
        users.add(user);
        usersMap.put("firstName",user.getFirstName());
        usersMap.put("secondName",user.getSecondName());
        usersMap.put("age",user.getAge());
        usersMap.put("passport",user.getPassport());
    }

    @Test
    public void testGetUserById_shouldReturnUserWhenValidId() {
        when(userRepository.findUserById(1L)).thenReturn(user);
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
        when(userRepository.findUserById(1L)).thenReturn(null);

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
    public void testGetAllAdultUsers_shouldReturnNoAdultUsersList() {
        User adultUser = users.get(0);
        adultUser.setAge(14);
        users.add(adultUser);
        users.remove(0);

        when(userRepository.findAll()).thenReturn(users);

        assertFalse(userService.getAllUsers().get(0).getAge() >= 18);
    }

    @Test
    public void testGetAllAdultUsers_shouldReturnAdultUsersList() {
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
    public void testCreateUser_shouldThrowWhenUserNull() {
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.createUser(null));
        assertEquals(USERCANNOTBEEMPTY, exception.getMessage());
    }

    @Test
    public void testCreateUser_shouldThrowWhenUserAlreadyExists() {
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
    public void testDeleteUserByPassport_shouldThrowWhenPassportIsNull() {
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.deleteUserByPassport(null));
        assertEquals("Passport cannot be null", exception.getMessage());
    }

    @Test
    public void testDeleteUserByPassport_shouldThrowWhenUserNotFound() {
        when(userRepository.deleteUserByPassport("4321-543552")).thenReturn(0);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.deleteUserByPassport("4321-543552"));

        assertEquals(USERNOTFOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testUpdateFullUser_shouldUpdateUser() {
        when(userRepository.updateUser(user.getId(), user.getFirstName(),
                user.getSecondName(), user.getAge(), user.getPassport())).thenReturn(1);

        assertEquals(HttpStatus.OK,userService.updateUser(user));
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
                user.getSecondName(), user.getAge(), user.getPassport())).thenReturn(0);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.updateUser(user));

        assertEquals("User not updated", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testUpdateUserById_shouldUpdateUser() {
        when(userRepository.findUserById(1L)).thenReturn(user);
        when(userRepository.updateUserById(1L,
                user.getFirstName(),user.getSecondName(),
                user.getAge(),user.getPassport()))
                .thenReturn(1);

        assertEquals(user,userService.getUserById(1L));
        assertEquals(HttpStatus.OK,userService.updateUserById(1L,usersMap));
    }


    @Test
    public void testUpdateUserById_shouldThrowWhenUserNotFound() {
        when(userRepository.findUserById(1L)).thenReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(1L));

        assertEquals(USERNOTFOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testUpdateUserById_shouldThrowWhenUserNotUpdated(){
        when(userRepository.findUserById(1L)).thenReturn(user);
        when(userRepository.updateUserById(1L,user.getFirstName()
                ,user.getSecondName(),user.getAge(),user.getPassport())).thenReturn(0);

        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> userService.updateUserById(1L,usersMap));

        assertEquals(user,userService.getUserById(1L));
        assertEquals("User not updated", userNotFoundException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, userNotFoundException.getStatus());
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
