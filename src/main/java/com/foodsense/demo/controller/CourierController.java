package com.foodsense.demo.controller;

import com.foodsense.demo.blob.service.AzureBlobService;
import com.foodsense.demo.dao.CourierDAO;
import com.foodsense.demo.model.Courier;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/foodsense/api/v0.0.1/courier")
public class CourierController {
    @Autowired
    private CourierDAO courierDAO;

    @Autowired
    private AzureBlobService azureBlobService;

    private static final Logger log = LoggerFactory.getLogger(CourierController.class);

    @GetMapping("/get")
    public ResponseEntity<List<Courier>> findAllCourier(){
        log.info("Fetching all courier...");
        List<Courier> couriers = courierDAO.findAllCourier();
        if(!couriers.isEmpty()){
            log.info("Found all couriers: {}", couriers.size());
            return ResponseEntity.ok(couriers);
        } else {
            log.warn("No courier found in the database");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(couriers);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Courier> findCourierByID(@PathVariable int id){
        log.info("Searching for courier with ID: {}", id);
        Courier courier = courierDAO.findCourierByID(id);
        if(courier != null){
            log.info("Found courier with ID: {}", id);
            return ResponseEntity.ok(courier);
        } else {
            log.warn("No courier found in the database with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(courier);
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<String> insertCourier(@RequestBody Courier courier){
        log.info("Creating courier account...");
        int result = courierDAO.insertCourier(courier);
        if (result > 0) {
            log.info("Successfully creating courier account with ID: {}", result);
            return ResponseEntity.status(HttpStatus.CREATED).body("Courier inserted successfully with user id = " + result);
        } else {
            log.warn("Failed to create courier account");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Courier failed to be deleted");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCourierByID(@PathVariable int id){
        log.info("Searching for courier with ID: {}", id);
        int result = courierDAO.deleteCourierByID(id);
        if (result > 0){
            log.info("Customer user with ID {} has been deleted", id);
            return ResponseEntity.ok("Courier successfully deleted");
        } else {
            log.warn("Failed deleting courier with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Courier failed to be deleted");
        }
    }

    @PutMapping(value="/update/image/{id}", consumes={"multipart/form-data"})
    public ResponseEntity<String> uploadImage(@PathVariable int id, @RequestPart("file") MultipartFile file){
        try {
            log.info("Uploading courier with ID {} iamge", id);
            Courier uploading = courierDAO.findCourierByID(id);
            if(uploading != null) {
                log.info("Courier with ID {} is inserting an image", id);

                String imageUrl = azureBlobService.uploadImage(file);
                if(imageUrl != null) {
                    log.info("Courier with ID {} successfully inserted an image", id);
                    uploading.setImageUrl(imageUrl);
                    courierDAO.uploadImage(uploading, id);

                    return ResponseEntity.ok("Courier successfully set an image");
                } else {
                    log.warn("Courier with ID {} failed uploaded an image", id);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to set an image");
                }
            } else {
                log.warn("Courier with ID {} is not found in the database", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Courier isn't exist");
            }
        } catch (IOException e) {
            log.error("Error when trying to upload an image: ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem occured on input/output");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCourierByID(@RequestBody Courier courier, @PathVariable int id){
        log.info("Updating courier bu ID: {}", id);
        int result = courierDAO.updateCourierByID(courier, id);
        if (result > 0) {
            log.info("Courier with ID {} has been updated", id);
            return ResponseEntity.ok("Courier successfully updated");
        } else {
            log.warn("Failed updating courier with ID: {}", id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No courier updated");
        }
    }
}