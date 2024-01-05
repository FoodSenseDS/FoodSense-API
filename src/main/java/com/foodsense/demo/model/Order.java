package com.foodsense.demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="orderr")
public class Order {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="order_id")
    private long order_id;

    @Column(name="order_status")
    private boolean orderStatus = true;

    @OneToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name="courier_id")
    @JsonIgnoreProperties("orderr")
    private Courier courier;

    @OneToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name="customer_id")
    @JsonIgnoreProperties("orderr")
    private Customer customer;

    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, targetEntity=OrderItem.class)
    @JsonIgnoreProperties("orderr")
    private List<OrderItem> orderItems;

    public Order(){}

    public Order(long order_id, boolean orderStatus, List<OrderItem> orderItems){
        setOrderId(order_id);
        setOrderStatus(orderStatus);
        setOrderItem(orderItems);
    }

    public Order(Customer customer, List<OrderItem> orderItems) {
        setCustomer(customer);
        setOrderItem(orderItems);
    }

    public void isOrderred(boolean orderStatus){
        this.orderStatus = orderStatus;
    }

    public long getOrderId(){
        return order_id;
    }

    public void setOrderId(long order_id){
        this.order_id = order_id;
    }

    public boolean getOrderStatus(){
        return orderStatus;
    }

    public void setOrderStatus(boolean orderStatus){
        this.orderStatus = orderStatus;
    }

    public Courier getCourier(){
        return courier;
    }

    public void setCourier(Courier courier){
        this.courier = courier;
    }

    public Customer getCustomer(){
        return customer;
    }

    public void setCustomer(Customer customer){
        this.customer = customer;
    }

    public List<OrderItem> getOrderItem(){
        return orderItems;
    }

    public void setOrderItem(List<OrderItem> orderItems){
        this.orderItems = orderItems;
    }

    public void isOrderrer(boolean orderStatus){
        this.orderStatus = orderStatus;
    }
}
