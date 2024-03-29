package com.foodsense.demo.controller;

import com.foodsense.demo.dao.SellerDAO;
import com.foodsense.demo.model.Product;
import com.foodsense.demo.model.Seller;
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
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/foodsense/api/v0.0.1/seller")
public class SellerController {
    @Autowired
    private SellerDAO sellerDAO;

    @Autowired
    private AzureBlobService azureBlobService;

    private static final Logger log = LoggerFactory.getLogger(SellerController.class);

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Seller>> findAllSellers(){
        log.info("Fetching all sellers...");
        List<Seller> sellers = sellerDAO.findAllSeller();
        if(!sellers.isEmpty()){
            log.info("Found all sellers: {}", sellers.size());
            return ResponseEntity.ok(sellers);
        } else {
            log.warn("No sellers found in the database");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(sellers);
        }
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Seller> findSellerByID(@PathVariable long id){
        log.info("Searching for seller with ID: {}", id);
        Seller seller = sellerDAO.findSellerByID(id);
        if(seller != null){
            log.info("Found seller with ID: {}", id);
            return ResponseEntity.ok(seller);
        } else {
            log.warn("No seller found in the database with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(seller);
        }
    }

    @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> insertSeller(@RequestBody Seller seller){
        log.info("Creating seller account...");
        int result = sellerDAO.insertSeller(seller);
        if(result > 0){
            log.info("Successfully creating seller with ID: {}", result);
            return ResponseEntity.status(HttpStatus.CREATED).body("Seller inserted successfully with user id = " + result);
        } else {
            log.warn("Failed creating seller account");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert seller");
        }
    }

    @DeleteMapping(value = "delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteSellerByID(@PathVariable long id) {
        log.info("Searching for seller with ID: {}", id);
        int result = sellerDAO.deleteSellerByID(id);
        if(result > 0){
            log.info("Seller user with ID {} has been deleted", id);
            return ResponseEntity.ok("Seller successfully deleted");
        } else {
            log.warn("Failed deleting seller with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller failed to be deleted");
        }
    }

    @PutMapping(value="/update/image/{id}", consumes={"multipart/form-data"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadImage(@PathVariable long id, @RequestPart("file") MultipartFile file) {
        try {
            log.info("Updloading seller with ID {} image", id);
            Seller uploading = sellerDAO.findSellerByID(id);
            if(uploading != null) {
                log.info("Seller with ID {} is inserting an image", id);

                String imageUrl = azureBlobService.uploadImage(file);
                if(imageUrl != null) {
                    log.info("Seller with ID {} successfully inserted an image", id);
                    uploading.setImageUrl(imageUrl);
                    sellerDAO.uploadImage(uploading, id);

                    return ResponseEntity.ok("Seller successfully set an image");
                } else {
                    log.warn("Seller with ID {} failed uploaded an image", id);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to set an image");
                }
            } else {
                log.warn("Seller with ID {} is not found in the database", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller isn't exist");
            }
        } catch (IOException e) {
            log.error("Error when trying to upload an image: ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem occured on input/output");
        }
    }

    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateSellerByID(@RequestBody Seller seller, @PathVariable int id){
        log.info("Updating seller by ID: {}", id);
        int result = sellerDAO.updateSellerByID(seller, id);
        if (result > 0) {
            log.info("Seller with ID {} has been updated", id);
            return ResponseEntity.ok("Seller successfully updated");
        } else {
            log.warn("Failed updating seller with ID: {}", id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No seller updated");
        }
    }

    @GetMapping(value="/get/product/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> getProductBySellerID(@PathVariable long id){
        log.info("Fetch all product with specific seller {}", id);
        List<Product> products = sellerDAO.findProductBySellerID(id);
        if (!products.isEmpty()){
            log.info("Get seller products");
            return ResponseEntity.ok(products);
        } else {
            log.warn("Failed get seller products");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(products);
        }
    }
}
