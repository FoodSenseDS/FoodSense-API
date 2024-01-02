package com.foodsense.demo.repository;

import com.foodsense.demo.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductJPARepo extends JpaRepository<Product, Long>{
}
