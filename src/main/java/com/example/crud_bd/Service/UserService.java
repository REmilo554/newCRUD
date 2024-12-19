package com.example.crud_bd.Service;

import com.example.crud_bd.AspectLogger.Loggable;
import com.example.crud_bd.Entity.User;
import com.example.crud_bd.Exceptions.UserNotFoundException;
import com.example.crud_bd.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.crud_bd.Validate.Validate.validateUserMap;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(Long id) {
        if (id == null) {
            throw new UserNotFoundException("Id cannot be empty", HttpStatus.BAD_REQUEST);
        }
        Optional<User> userOptional = Optional.ofNullable(userRepository.getUserById(id));
        return userOptional.orElseThrow(() -> new UserNotFoundException("User not found", HttpStatus.NOT_FOUND));
    }

    public List<User> getAllUsers() {
        return Optional.ofNullable(userRepository.findAll())
                .orElseThrow(() -> new UserNotFoundException("Users not found", HttpStatus.NOT_FOUND));
    }

    public List<User> getAllAdultUser() {
        return Optional.ofNullable(userRepository.findAllAdultUsers())
                .orElseThrow(() -> new UserNotFoundException("Users not found", HttpStatus.NOT_FOUND));
    }

    @Loggable(message = "Create user")
    public User createUser(User user) {
        if (user == null) {
            throw new UserNotFoundException("User cannot be empty", HttpStatus.BAD_REQUEST);
        }
        return Optional.of(userRepository.save(user))
                .orElseThrow(() -> new UserNotFoundException("User not found", HttpStatus.BAD_REQUEST));
    }

    @Loggable(message = "Deleting user")
    public User deleteUserById(Long id) {
        if (id == null) {
            throw new UserNotFoundException("Id cannot be empty", HttpStatus.NOT_FOUND);
        }
        Optional<User> userOptional = Optional.ofNullable(userRepository.getUserById(id));
        if (userOptional.isPresent()) {
            userRepository.deleteUserById(id);
        }
        return userOptional.orElseThrow(() -> new UserNotFoundException("User not found", HttpStatus.NOT_FOUND));
    }

    @Loggable(message = "Deleting user")
    public User deleteUserByPassport(String passport) {
        if (passport == null) {
            throw new UserNotFoundException("Passport cannot be null", HttpStatus.NOT_FOUND);
        }
        Optional<User> userOptional = Optional.ofNullable(userRepository.getUserByPassport(passport));
        if (userOptional.isPresent()) {
            userRepository.deleteUserByPassport(passport);
        }
        return userOptional.orElseThrow(() -> new UserNotFoundException("User not found", HttpStatus.NOT_FOUND));
    }

    @Loggable(message = "Update user")
    public void updateUser(User user) {
        if (user == null) {
            throw new UserNotFoundException("User not found", HttpStatus.NOT_FOUND);
        }
        userRepository.updateUser(user.getId(), user.getFirstName(),
                user.getSecondName(), user.getAge(), user.getPassport());
    }

    @Loggable(message = "User data update")
    public void updateUserById(Long id, Map<String, Object> dataUser) {
        User user = userRepository.getUserById(id);
        user = validateUserMap(user, dataUser);
        userRepository.updateUserById(user.getId(), user.getFirstName(),
                user.getSecondName(), user.getAge(), user.getPassport());
    }

}
