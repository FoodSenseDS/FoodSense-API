package com.foodsense.demo.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.foodsense.demo.blob.service.AzureBlobService;
import com.foodsense.demo.dao.ProductDAO;
import com.foodsense.demo.model.Product;

@RestController
@RequestMapping("/foodsense/api/v0.0.1/product")
public class ProductController {
    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private AzureBlobService azureBlobService;

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("/get")
    public ResponseEntity<List<Product>> findAllProduct(){
        log.info("Fetching all product...");
        List<Product> products = productDAO.findAllProduct();
        if (!products.isEmpty()) {
            log.info("Found all products: {}", products.size());
            return ResponseEntity.ok(products);
        } else {
            log.warn("No product found in the database");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(products);
        }
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<Product> findProductByID(@PathVariable int id){
        log.info("Searching for product with ID: {}", id);
        Product product = productDAO.findProductByID(id);
        if (product != null) {
            log.info("Found product with ID: {}", id);
            return ResponseEntity.ok(product);
        } else {
            log.warn("No product found in the database with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(product);
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<String> insertProduct(@RequestParam(name="sellerId") long id, @RequestBody Product product){
        log.info("Inserting new product...");
        int result = productDAO.insertProduct(product, id);
        if (result > 0) {
            log.info("Successfully insert product with ID: {}", result);
            return ResponseEntity.status(HttpStatus.CREATED).body("Product inserted successfully with product id = " + result);
        } else {
            log.warn("Failed inserting product");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert product");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProductByID(@PathVariable int id){
        log.info("Searching for product with ID: {}", id);
        int result = productDAO.deleteProductByID(id);
        if (result > 0) {
            log.info("Product with ID {} has been deleted", id);
            return ResponseEntity.ok("Product successfully deleted");
        } else {
            log.warn("Failed to deleting product with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product falied to be deleted");
        }
    }

    @PutMapping(value="/update/image/{id}", consumes={"multipart/form-data"})
    public ResponseEntity<String> uploadImage(@PathVariable int id, @RequestPart("file") MultipartFile file){
        try {
            log.info("Uploading product with ID {} image", id);
            Product uploading = productDAO.findProductByID(id);
            if (uploading != null) {
                log.info("Inserting an image for product with ID: {}", id);
                
                String imageUrl = azureBlobService.uploadImage(file);
                if (imageUrl != null) {
                    uploading.setImageUrl(imageUrl);
                    productDAO.uploadProductImage(uploading, id);

                    return ResponseEntity.ok("Successfully set a product image");
                } else {
                    log.warn("Failed to set an image on product with ID: {}", id);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed set an image product");
                }
            } else {
                log.warn("Product with ID {} is not found in the database");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product isn't exist");
            }
        } catch (IOException e) {
            log.error("Error when trying to upload an image: ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem occured on input/output");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProductInfoByID(@RequestBody Product product, @PathVariable int id){
        log.info("Updating product by ID: {}", id);
        int result = productDAO.updateProductInfoByID(product, id);
        if (result > 0) {
            log.info("Product with ID {} info has been updated", id);
            return ResponseEntity.ok("Product info successfully updated");
        } else {
            log.warn("Failed updating product with ID {} info", id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No product updated");
        }
    }

    @PutMapping("/update/stock/{id}")
    public ResponseEntity<String> updateProductStockByID(@RequestBody Product product, @PathVariable int id){
        log.info("Updating stock of product with ID: {}", id);
        int result = productDAO.updateProductStockByID(product, id);
        if (result > 0) {
            log.info("The amount of products with ID {} has been changed", id);
            return ResponseEntity.ok("Amount of products successfully changed");
        } else {
            log.warn("Failed changing the amount of products with ID {}", id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product stock isn't changed");
        }
    }
}
