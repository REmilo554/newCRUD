package com.example.userservice.Controller;


import com.example.userservice.DTO.PassportDTO;
import com.example.userservice.Entity.User;
import com.example.userservice.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<User> findUserById(@Validated @PathVariable Long id) {
        return new ResponseEntity<>(userService.findUserById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> findAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/allAdult")
    public ResponseEntity<List<User>> findAllAdultUsers() {
        return new ResponseEntity<>(userService.findAllAdultUsers(), HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<User> save(@Validated @RequestBody User user) {
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    //http://localhost:8080/users/
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.deleteUserById(id), HttpStatus.OK);
    }

    //http://localhost:8080/users/delete
    @DeleteMapping("/delete")
    public ResponseEntity<User> deleteUserByPassport(@RequestHeader("passport") String passport, @Validated PassportDTO passportDTO) {
        passportDTO.setPassport(passport);
        return new ResponseEntity<>(userService.deleteUserByPassport(passport), HttpStatus.OK);
    }

    /**
     * http://localhost:8080/users/put
     * <p>
     * {
     * "id":4,
     * "firstName": "Kek",
     * "secondName": "Lol",
     * "age": 14,
     * "passport": "9999-576657"
     * }
     */
    @PutMapping("/put")
    public ResponseEntity<User> updateUser(@Validated @RequestBody User user) {
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.updateUser(user), HttpStatus.OK);
    }

    //  http://localhost:8080/users/patch/3
    @PatchMapping("/patch/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable Long id, @RequestBody Map<String, Object> dataUser) {
        if (dataUser == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.updateUserById(id, dataUser), HttpStatus.OK);
    }

}
