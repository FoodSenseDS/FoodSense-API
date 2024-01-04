package com.foodsense.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.foodsense.demo.enumeration.RoleCategory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @OneToOne(mappedBy="courier", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JsonIgnoreProperties("courier")
    private Order deliveredOrder;

    public Courier(){}

    public Courier(long courier_id, String email, String password, String phoneNo, String fullname, String address, RoleCategory role, String imageUrl, boolean onDelivery){
        super(email, password, phoneNo, fullname, address, role);
        setCourierId(courier_id);
        setImageUrl(imageUrl);
        setOnDelivery(onDelivery);
    }

    public Courier(String email, String password, String phoneNo, String fullname, String address, RoleCategory role){
        super(email, password, phoneNo, fullname, address, role);
    }

    public Order getDeliveredOrder(){
        return deliveredOrder;
    }

    public void setDeliveredOrder(Order delivereOrder){
        this.deliveredOrder = delivereOrder;
    } 

    public long getCourierId(){
        return courier_id;
    }

    public void setCourierId(long courier_id){
        this.courier_id = courier_id;
    }

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
