package com.foodsense.demo.repository;

import java.util.List;

import com.foodsense.demo.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductJPARepo extends JpaRepository<Product, Long>{
    @Query("SELECT p FROM Product p WHERE p.seller.id = :sellerId")
    List<Product> findProductBySellerID(@Param("sellerId") long seller_id);
}
