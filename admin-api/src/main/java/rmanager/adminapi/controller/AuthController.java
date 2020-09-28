package rmanager.adminapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rmanager.commons.entity.User;
import rmanager.commons.service.UserService;

import javax.servlet.ServletException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) throws ServletException {
        String token = userService.login(user);
        if (token != null) {
            Map<String, String> wrapper = new HashMap<>();
            wrapper.put("token", token);
            return new ResponseEntity<>(wrapper, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> logout(@RequestHeader(name = "Authentication") String token) {
        HttpStatus httpStatus;
        if (userService.logout(token)) {
            httpStatus = HttpStatus.OK;
        } else {
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(httpStatus);
    }

    @GetMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity status(@RequestHeader(name = "Authentication") String token) {
        HttpStatus httpStatus;
        User user = userService.getUserByToken(token);
        if (user != null) {
            httpStatus = HttpStatus.OK;
        } else {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity(httpStatus);
    }

}
