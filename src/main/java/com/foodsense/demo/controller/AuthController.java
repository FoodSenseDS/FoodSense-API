package com.foodsense.demo.controller;

import com.foodsense.demo.dao.AuthDAO;
import com.foodsense.demo.model.User;

import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/foodsense/api/v0.0.1/auth")
public class AuthController {
    @Autowired
    private AuthDAO authDAO;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerUser(@RequestBody User user){
        log.info("Registering user...");
        int result = authDAO.register(user.getEmail(), user.getPassword(), user.getphone_number(), user.getFullName(), user.getAddress(), user.getRole());
        if(result > 0) {
            log.info("Successfully registering a user with ID: {}", result);
            return ResponseEntity.status(HttpStatus.CREATED).body("Your User ID is: " + result);
        } else {
            log.warn("Failed registering a user account");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user");
        }
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> loginUser(@RequestBody User user) {
        log.info("Attempting user login...");
        User result = authDAO.login(user.getEmail(), user.getPassword(), user.getRole());
        if (result != null) {
            log.info("User with email {} successfully login", result.getEmail());
            return ResponseEntity.ok(result);
        } else {
            log.warn("User not existed, try register first");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
    }
}
