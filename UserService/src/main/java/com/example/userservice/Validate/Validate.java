package com.example.userservice.Validate;


import com.example.userservice.Entity.User;
import com.example.userservice.Exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;

import java.util.Map;

//TODO сделать проверки элегантней или сделать стратегию
public class Validate {
    public static User validateUserMap(User user, Map<String, Object> maps) {
        removeNulls(maps);
        if (!maps.isEmpty()) {
            if (maps.containsKey("firstName")) {
                Object firstName = maps.get("firstName");
                if (firstName instanceof String) {
                    user.setFirstName((String) firstName);
                }
            }
            if (maps.containsKey("secondName")) {
                Object secondName = maps.get("secondName");
                if (secondName instanceof String) {
                    user.setSecondName((String) secondName);
                }
            }
            if (maps.containsKey("age")) {
                Object age = maps.get("age");
                if (age instanceof Integer) {
                    user.setAge((Integer) age);
                }
            }
            if (maps.containsKey("passport")) {
                Object passport = maps.get("passport");
                if (passport instanceof String && ((String) passport).length() == 11) {
                    user.setPassport((String) passport);
                }
            }
            return user;
        } else {
            throw new UserNotFoundException("User fields are missing", HttpStatus.BAD_REQUEST);
        }
    }


    private static void removeNulls(Map<String, Object> maps) {
        maps.keySet().removeIf(key -> checkNull(maps.get(key)));
    }

    public static boolean checkNull(Object object) {
        return object == null;
    }
}
