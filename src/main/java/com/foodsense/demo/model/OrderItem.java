package com.foodsense.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long order_item_id;

    private long productId;

    private String productName;

    private int quantity;

    private double price;

    public OrderItem(){}

    public OrderItem(long order_item_id, long productId, String productName, int quantity, double price){
        setOrderItemId(order_item_id);
        setProductId(productId);
        setProductName(productName);
        setQuantity(quantity);
        setPrice(price);
    }

    public long getOrderItemId(){
        return order_item_id;
    }

    public void setOrderItemId(long order_item_id){
        this.order_item_id = order_item_id;
    }

    public long getProductId(){
        return productId;
    }

    public void setProductId(long productId){
        this.productId = productId;
    }

    public String getProductName(){
        return productName;
    }

    public void setProductName(String productName){
        this.productName = productName;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public double getPrice(){
        return price;
    }

    public void setPrice(double price){
        this.price = price;
    }
}
