package com.foodsense.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodsense.demo.model.Admin;
import com.foodsense.demo.dao.AdminDAO;

@RestController
@RequestMapping("/foodsense/api/v0.0.1/admin")
public class AdminController {
    @Autowired
    private AdminDAO adminDAO;

    private static final Logger log =  LoggerFactory.getLogger(AdminController.class);

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Admin>> findAllAdmin(){
        log.info("Fetching all admin...");
        List<Admin> admins = adminDAO.findAllAdmin();
        if (!admins.isEmpty()) {
            log.info("Found all admins: {}", admins.size());
            return ResponseEntity.ok(admins);
        } else {
            log.warn("No admin found in the database");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(admins);
        }
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Admin> findAdminByID(@PathVariable int id){
        log.info("Searching admin with ID: {}", id);
        Admin admin = adminDAO.findAdminByID(id);
        if (admin != null){
            log.info("Found admin qith ID: {}", id);
            return ResponseEntity.ok(admin);
        } else {
            log.warn("No admin found in the database with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(admin);
        }
    }

    @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> insertAdmin(@RequestBody Admin admin){
        log.info("Creating admin account...");
        int result = adminDAO.insertAdmin(admin);
        if (result > 0) {
            log.info("Successfully creating admin with ID: {}", result);
            return ResponseEntity.status(HttpStatus.CREATED).body("Admin inserted successfully with user id = " + result);
        } else {
            log.warn("Failed creating admin account");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert admin");
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteAdminByID(@PathVariable int id){
        log.info("Searching for admin with ID: {}", id);
        int result = adminDAO.deleteAdminByID(id);
        if (result > 0) {
            log.info("Admin user with ID {} has been deleted", id);
            return ResponseEntity.ok("Admin successfully deleted");
        } else {
            log.warn("Failed deleting admin with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin failed to be deleted");
        }
    }

    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateAdminByID(@RequestBody Admin admin, @PathVariable int id){
        log.info("Updating admin with ID: {}", id);
        int result = adminDAO.updateAdminByID(admin, id);
        if(result > 0){
            log.info("Admin with ID {} has been updated", id);
            return ResponseEntity.ok("Admin successfully updated");
        } else {
            log.warn("Failed updating admin with ID: {}", id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No admin updated");
        }
    }
}
