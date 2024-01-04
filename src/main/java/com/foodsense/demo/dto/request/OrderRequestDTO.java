package com.foodsense.demo.dto.request;

import java.util.List;

import com.foodsense.demo.model.OrderItem;

public class OrderRequestDTO {
    private List<OrderItem> orderItems;

    public OrderRequestDTO(){}

    public OrderRequestDTO(List<OrderItem> orderItems){
        setOrderItems(orderItems);
    }

    public List<OrderItem> getOrderItems(){
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems){
        this.orderItems = orderItems;
    }
}
