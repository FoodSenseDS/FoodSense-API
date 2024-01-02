package com.foodsense.demo.model;

// import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.foodsense.demo.enumeration.RoleCategory;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="courier")
public class Courier extends User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="courier_id")
    private long courier_id;

    @Column(name="on_delivery")
    @JsonProperty("on_delivery")
    private boolean onDelivery = false;

    @Column(name="image_url")
    private String imageUrl;

    // @Column(name="delivered_order")
    // private List<Order> deliveredOrder;

    public Courier(){}

    public Courier(long courier_id, String email, String password, String phoneNo, String fullname, String address, RoleCategory role, String imageUrl, boolean onDelivery){
        super(email, password, phoneNo, fullname, address, role);
        // setCourierId(courier_id);
        setImageUrl(imageUrl);
        setOnDelivery(onDelivery);
    }

    public Courier(String email, String password, String phoneNo, String fullname, String address, RoleCategory role){
        super(email, password, phoneNo, fullname, address, role);
    }

    public long getCourierId(){
        return courier_id;
    }

    // public void setCourierId(long courier_id){
    //     this.courier_id = courier_id;
    // }

    // public void orderDelivered(List<Order> orders){
    //     this.deliveredOrder=orders;
    // }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean getOnDelivery(){
        return onDelivery;
    }

    public void setOnDelivery(boolean onDelivery){
        this.onDelivery = onDelivery;
    }

    @Override
    protected boolean isValidRoleForUser(RoleCategory role){
        return role == RoleCategory.COURIER;
    }
}
