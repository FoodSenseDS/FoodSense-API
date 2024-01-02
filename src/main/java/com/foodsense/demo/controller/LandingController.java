package com.foodsense.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/foodsense/api/v0.0.1")
public class LandingController {

    @GetMapping("/")
    public ResponseEntity<String> home(){
        String welcome_message = "Welcome to the FoodSense API!🙏";
        return ResponseEntity.ok(welcome_message);
    }
}
