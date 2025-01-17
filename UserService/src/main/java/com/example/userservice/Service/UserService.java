package com.example.userservice.Service;


import com.example.userservice.AspectLogger.Loggable;
import com.example.userservice.Entity.User;
import com.example.userservice.Exceptions.UserNotFoundException;
import com.example.userservice.Kafka.KafkaProducerService;
import com.example.userservice.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.userservice.Validate.Validate.validateUserMap;


//TODO добавить к методам проверки,что user.age > 14

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserService {

    private final KafkaProducerService kafkaProducerService;

    private final UserRepository userRepository;

    /**
     * запрос одиночных пользователей надо оборачивать в optional
     * т.к бд либо вернет пользователя
     * либо вернет null
     */
    @Cacheable(value = "myCache")
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public User findUserById(Long id) {
        if (id == null) {
            throw new UserNotFoundException("Id cannot be empty", HttpStatus.BAD_REQUEST);
        }
        Optional<User> userOptional = Optional.ofNullable(userRepository.findUserById(id));
        userOptional
                .ifPresent(user -> kafkaProducerService.sendMessage("User found", user));
        return userOptional
                .orElseThrow(() -> new UserNotFoundException("User not found", HttpStatus.NOT_FOUND));
    }

    /**
     * запросы возвращающие листы не надо оборачивать
     * т.к. в любом случае возвращается лист
     * либо нормальный, либо empty
     */
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<User> findAll() {
        List<User> usersList = userRepository.findAll();
        if (usersList.isEmpty()) {
            throw new UserNotFoundException("Users not found", HttpStatus.NOT_FOUND);
        }
        return usersList;
    }


    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<User> findAllAdultUsers() {
        List<User> usersList = userRepository.findAllAdultUsers();
        if (usersList.isEmpty()) {
            throw new UserNotFoundException("Users not found", HttpStatus.NOT_FOUND);
        }
        return usersList;
    }

    /**
     * обернул в ofNullable т.к вылетает NPE если из репозитория возвращается null
     */
    @Cacheable(value = "myCache", key = "#user.firstName + #user.secondName")
    @Transactional
    @Loggable(message = "Create user")
    public User save(User user) {
        if (user == null) {
            throw new UserNotFoundException("User cannot be empty", HttpStatus.BAD_REQUEST);
        }
        return Optional.ofNullable(userRepository.save(user))
                .orElseThrow(() -> new UserNotFoundException("User not found", HttpStatus.BAD_REQUEST));
    }

    @Transactional
    @Loggable(message = "Deleting user")
    public User deleteUserById(Long id) {
        if (id == null) {
            throw new UserNotFoundException("Id cannot be empty", HttpStatus.NOT_FOUND);
        }
        User user = userRepository.deleteUserById(id);
        if (user == null) {
            throw new UserNotFoundException("User not found", HttpStatus.NOT_FOUND);
        }
        return user;
    }

    @Transactional
    @Loggable(message = "Deleting user")
    public User deleteUserByPassport(String passport) {
        if (passport == null) {
            throw new UserNotFoundException("Passport cannot be null", HttpStatus.NOT_FOUND);
        }
        User user = userRepository.deleteUserByPassport(passport);
        if (user == null) {
            throw new UserNotFoundException("User not found", HttpStatus.NOT_FOUND);
        }
        return user;
    }

    @Transactional
    @Loggable(message = "Update user")
    public User updateUser(User user) {
        if (user == null) {
            throw new UserNotFoundException("User not found", HttpStatus.NOT_FOUND);
        }
        return Optional.ofNullable(userRepository.updateUser(user.getId(),
                        user.getFirstName(), user.getSecondName(), user.getAge(),
                        user.getPassport()))
                .orElseThrow(() -> new UserNotFoundException("User not updated",
                        HttpStatus.NOT_FOUND));
    }

    @Transactional
    @Loggable(message = "User data update")
    public User updateUserById(Long id, Map<String, Object> dataUser) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new UserNotFoundException("User not found", HttpStatus.NOT_FOUND);
        }
        user = validateUserMap(user, dataUser);
        User returnedUser = userRepository.updateUserById(user.getId(), user.getFirstName(),
                user.getSecondName(), user.getAge(), user.getPassport());
        if (returnedUser == null) {
            throw new UserNotFoundException("User not updated", HttpStatus.NOT_FOUND);
        }
        return returnedUser;
    }
}
