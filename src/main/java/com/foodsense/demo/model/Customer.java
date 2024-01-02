package com.foodsense.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.foodsense.demo.enumeration.RoleCategory;

@Entity
@Table(name="customers")
public class Customer extends User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="customer_id")
    private long customer_id;

    @Column(name="image_url")
    private String imageUrl;

    public Customer(){}

    //this can retrieve customer id also
    public Customer(long customer_id, String email, String password, String phone_number, String fullname, String address, RoleCategory role, String imageUrl){
        super(email, password, phone_number, fullname, address, role);
        setCustomerId(customer_id);
        setImageUrl(imageUrl);
    }

    public Customer(String email, String password, String phone_number, String fullname, String address, RoleCategory role){
        super(email, password, phone_number, fullname, address, role);
    }

    public void setCustomerId(long customer_id){
        this.customer_id = customer_id;
    }

    public long getCustomerId(){
        return customer_id;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    protected boolean isValidRoleForUser(RoleCategory role){
        return role == RoleCategory.CUSTOMER;
    }
}