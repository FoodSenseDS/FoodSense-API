package com.foodsense.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodsense.demo.model.Order;

@Repository
public interface OrderJPARepo extends JpaRepository<Order, Long>{

}
