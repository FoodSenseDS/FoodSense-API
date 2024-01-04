package com.foodsense.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foodsense.demo.model.Order;
import com.foodsense.demo.model.OrderItem;

@Service
public interface OrderService {
    public List<Order> findAllOrder();

    public Order findOrderDetail(long id);

    public Order insertOrder(Order order);

    public double getTotal(List<OrderItem> orderItems);

    public int updateOrder(long courier_id, long order_id);
}
