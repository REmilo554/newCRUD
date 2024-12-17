package com.example.crud_bd.Service;

import com.example.crud_bd.Entity.User;
import com.example.crud_bd.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
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


    public User createUser(User user) {
        if(user == null){
            throw new RuntimeException("User not found");
        }
        return Optional.of(userRepository.save(user)).orElseThrow(RuntimeException::new);
    }

    public void deleteUserById(Long id) {
        if(id == null)
            throw new RuntimeException("User not found");
        userRepository.deleteUserById(id);
    }

    //FIXME НЕ НАХОДИТ ПО ПАСПОРТУ
    public void deleteUserByPassport(String passport) {
        if(passport == null) {
            throw new RuntimeException("Passport not found");
        }
        String d = passport.trim();
        int i = userRepository.deleteUserByPassport(d);
        log.atInfo().log("Deleting user by {} and {}", d,i);
        User user = userRepository.getUserByPassport(d);
        log.atInfo().log("Find user {}",user.toString());
    }

}
