package com.example.crud_bd.Controller;

import com.example.crud_bd.AspectLogger.Loggable;
import com.example.crud_bd.Entity.User;
import com.example.crud_bd.Repository.UserRepository;
import com.example.crud_bd.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * http://localhost:8080/users/
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/allAdult")
    public ResponseEntity<List<User>> getAllAdultUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<User> createUser(@Validated @RequestBody User user) {
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    //http://localhost:8080/users/
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteUser(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Integer i = userService.deleteUserById(id);
        return new ResponseEntity<>(i, HttpStatus.OK);
    }

    //http://localhost:8080/users/delete
    @DeleteMapping("/delete")
    public ResponseEntity<Integer> deleteUserByPassport(@Valid @RequestHeader String passport) {
        if (passport == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Integer i = userService.deleteUserByPassport(passport);
        return new ResponseEntity<>(i, HttpStatus.OK);
    }

    /**
    http://localhost:8080/users/put

    {
    "id":4,
    "firstName": "Kek",
    "secondName": "Lol",
    "age": 14,
    "passport": "9999-576657"
    }
     */
    @PutMapping("/put")
    public ResponseEntity<Void> updateUser(@Validated @RequestBody User user) {
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

  //  http://localhost:8080/users/patch/3
    @PatchMapping("/patch/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody Map<String,Object> dataUser) {
        if (dataUser == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.updateUserById(id,dataUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
