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

//TODO добавить к методам проверки,что user.age > 14

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserService {

    private final UserRepository userRepository;

    /**
     * запрос одиночных пользователей надо оборачивать в optional
     * т.к бд либо вернет пользователя
     * либо вернет null
     */
    public User getUserById(Long id) {
        if (id == null) {
            throw new UserNotFoundException("Id cannot be empty", HttpStatus.BAD_REQUEST);
        }
        Optional<User> userOptional = Optional.ofNullable(userRepository.getUserById(id));
        return userOptional.orElseThrow(() -> new UserNotFoundException("User not found", HttpStatus.NOT_FOUND));
    }

    /**
     * запросы возвращающие листы не надо оборачивать
     * т.к. в любом случае возвращается лист
     * либо нормальный, либо empty
     */
    public List<User> getAllUsers() {
        List<User> usersList = userRepository.findAll();
        if(usersList.isEmpty()){
            throw new UserNotFoundException("Users not found", HttpStatus.NOT_FOUND);
        }
        return usersList;
    }

    public List<User> getAllAdultUsers() {
        List<User> usersList = userRepository.findAllAdultUsers();
        if(usersList.isEmpty()){
            throw new UserNotFoundException("Users not found", HttpStatus.NOT_FOUND);
        }
        return usersList;
    }

    /**
     *обернул в ofNullable т.к вылетает NPE если из репозитория возвращается null
     */
    @Loggable(message = "Create user")
    public User createUser(User user) {
        if (user == null) {
            throw new UserNotFoundException("User cannot be empty", HttpStatus.BAD_REQUEST);
        }
        return Optional.ofNullable(userRepository.save(user))
                .orElseThrow(() -> new UserNotFoundException("User not found", HttpStatus.BAD_REQUEST));
    }

    @Loggable(message = "Deleting user")
    public HttpStatus deleteUserById(Long id) {
        if (id == null) {
            throw new UserNotFoundException("Id cannot be empty", HttpStatus.NOT_FOUND);
        }
        Integer countOfDelete = userRepository.deleteUserById(id);
        if(countOfDelete == 0){
            throw new UserNotFoundException("User not found", HttpStatus.NOT_FOUND);
        }
        return HttpStatus.OK;
    }

    @Loggable(message = "Deleting user")
    public HttpStatus deleteUserByPassport(String passport) {
        if (passport == null) {
            throw new UserNotFoundException("Passport cannot be null", HttpStatus.NOT_FOUND);
        }
        Integer countOfDelete = userRepository.deleteUserByPassport(passport);
        if(countOfDelete == 0){
            throw new UserNotFoundException("User not found", HttpStatus.NOT_FOUND);
        }
        return HttpStatus.OK;
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
