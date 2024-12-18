package com.example.crud_bd.Service;

import com.example.crud_bd.AspectLogger.Loggable;
import com.example.crud_bd.Entity.User;
import com.example.crud_bd.Repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;


    public User getUserById(Integer id) {
        if (id == null) {
            throw new RuntimeException("User not found");
        }
        return userRepository.getUserById(id);
    }

    public List<User> getAllUsers() {
        return Optional.ofNullable(userRepository.findAll()).orElseThrow(RuntimeException::new);
    }

    public List<User> getAllAdultUser() {
        return Optional.ofNullable(userRepository.findAllAdultUsers()).orElseThrow(RuntimeException::new);
    }

    @Loggable(message = "Create user",level = "INFO")
    public User createUser(User user) {
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return Optional.of(userRepository.save(user)).orElseThrow(RuntimeException::new);
    }

    @Loggable(message = "Deleting user",level = "INFO")
    public Integer deleteUserById(Long id) {
        if (id == null)
            throw new RuntimeException("User not found");
        return userRepository.deleteUserById(id);
    }

    @Loggable(message = "Deleting user",level = "INFO")
    public Integer deleteUserByPassport(String passport) {
        if (passport == null) {
            throw new RuntimeException("Passport not found");
        }
        return userRepository.deleteUserByPassport(passport);
    }
}
