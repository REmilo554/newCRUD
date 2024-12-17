package com.example.crud_bd.Service;

import com.example.crud_bd.Entity.User;
import com.example.crud_bd.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Integer id) {
        if(id == null){
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
}
