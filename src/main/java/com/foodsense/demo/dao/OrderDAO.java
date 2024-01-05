package com.foodsense.demo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.foodsense.demo.model.Order;
import com.foodsense.demo.model.OrderItem;
import com.foodsense.demo.model.Product;
import com.foodsense.demo.repository.OrderJPARepo;
import com.foodsense.demo.repository.ProductJPARepo;
import com.foodsense.demo.service.OrderService;

@Repository
public class OrderDAO implements OrderService{

    @Autowired
    private OrderJPARepo orderJPARepo;

    @Autowired
    private ProductJPARepo productJPARepo;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Order> findAllOrder(){
        return orderJPARepo.findAll();
    }

    @Override
    public Order findOrderDetail(long id){
        Optional<Order> order = orderJPARepo.findById(id);
        return order.isPresent() ? order.get() : null;
    }

    @Override
    public Order insertOrder(Order order){
        return orderJPARepo.save(order);
    }

    @Override
    public double getTotal(List<OrderItem> orderItems){
        double totalPrice = 0;
        double singleCart = 0;
        int availableQuantity = 0;

        for (OrderItem oi : orderItems) {
            long prodductId = oi.getProductId();
            Optional<Product> product = productJPARepo.findById(prodductId);sss
            if (product.isPresent()) {
                Product productInit = product.get();
                if (productInit.getProductStock() < oi.getQuantity()) {
                    singleCart = productInit.getProductUnitPrice() * productInit.getProductStock();
                    oi.setQuantity(productInit.getProductStock());
                } else {
                    singleCart = oi.getQuantity() * productInit.getProductUnitPrice();
                    availableQuantity = productInit.getProductStock() - oi.getQuantity();
                }

                totalPrice = totalPrice + singleCart;
                productInit.setProductStock(availableQuantity);
                availableQuantity=0;
                oi.setProductName(productInit.getProductName());
                oi.setPrice(singleCart);
                productJPARepo.save(productInit);
            }
        }
        return totalPrice;
    }

    @Override
    public int updateOrder(long courier_id, long order_id) {
        String query = "UPDATE orderr SET courier_id=? WHERE order_id=?";
        return jdbcTemplate.update(query, courier_id, order_id);
    }
}
