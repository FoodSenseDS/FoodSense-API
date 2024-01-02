package com.foodsense.demo.controller;

import com.foodsense.demo.dao.CustomerDAO;
import com.foodsense.demo.model.Customer;
import com.foodsense.demo.blob.service.AzureBlobService;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/foodsense/api/v0.0.1/customer")
public class CustomerController {
    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private AzureBlobService azureBlobService;

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    @GetMapping("/get")
    public ResponseEntity<List<Customer>> findAllCustomers(){
        log.info("Fetching all customers...");
        List<Customer> customers = customerDAO.findAllCustomer();
        if (!customers.isEmpty()) {
            log.info("Found all customers: {}", customers.size());
            return ResponseEntity.ok(customers); 
        } else {
            log.warn("No customer found in the database");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(customers);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Customer> findCustomerByID(@PathVariable int id){
        log.info("Searching for customer with ID: {}", id);
        Customer customer = customerDAO.findCustomerByID(id);
        if (customer != null) {
            log.info("Found customer with ID: {}", id);
            return ResponseEntity.ok(customer);
        } else {
            log.warn("No customer found in the database with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(customer);
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<String> insertCustomer(@RequestBody Customer customer){
        log.info("Creating customer account...");
        int result = customerDAO.insertCustomer(customer);
        if (result > 0) {
            log.info("Successfully creating customer account with ID: {}", result);
            return ResponseEntity.status(HttpStatus.CREATED).body("Customer inserted successfully with user id = " + result);
        } else {
            log.warn("Failed creating customer account");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert customer");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCustomerByID(@PathVariable int id){
        log.info("Searching for customer with ID: {}", id);
        int result = customerDAO.deleteCustomerByID(id);
        if (result > 0) {
            log.info("Customer user with ID {} has been deleted", id);
            return ResponseEntity.ok("Customer successfully deleted");
        } else {
            log.warn("Failed deleting customer with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer failed to be deleted");
        }
    } 

    
    @PutMapping(value="/update/image/{id}", consumes={"multipart/form-data"})
    public ResponseEntity<String> uploadImage(@PathVariable int id, @RequestPart("file") MultipartFile file){
        try {
            log.info("Uploading customer with ID {} image", id);
            Customer uploading = customerDAO.findCustomerByID(id);
            if(uploading != null) {
                log.info("Customer with ID {} is inserting an image", id);

                String imageUrl = azureBlobService.uploadImage(file);
                if(imageUrl != null) {
                    log.info("Customer with ID {} successfully inserted an image", id);
                    uploading.setImageUrl(imageUrl);
                    customerDAO.uploadImage(uploading, id);

                    return ResponseEntity.ok("Customer successfully set an image");
                } else {
                    log.warn("Customer with ID {} failed uploaded an image", id);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to set an image");
                }
            } else {
                log.warn("Customer with ID {} is not found in the database", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer isn't exist");
            }
        } catch (IOException e) {
            log.error("Error when trying to upload an image: ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem occured on input/output");
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCustomerByID(@RequestBody Customer customer, @PathVariable int id){
        log.info("Updating customer by ID: {}", id);
        int result = customerDAO.updateCustomerByID(customer, id);
        if (result > 0){
            log.info("Customer with ID {} has been updated", id);
            return ResponseEntity.ok("Customer successfully updated");
        } else {
            log.warn("Failed updating customer with ID: {}", id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No customer updated");
        }
    }
}
