package com.foodsense.demo.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foodsense.demo.enumeration.RoleCategory;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name="seller")
public class Seller extends User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private long seller_id;

    @Column(name = "image_url")
    private String imageUrl;

    // @Column(name="product")
    @OneToMany(mappedBy="seller", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JsonIgnoreProperties("Seller")
    private List<Product> product;

    public Seller(){}

    //To Get Seller Id Also
    public Seller(long seller_id, String email, String password, String phoneNo, String fullname, String address, RoleCategory role, String imageUrl){
        super(email, password, phoneNo, fullname, address, role);
        setSellerId(seller_id);
        setImageUrl(imageUrl);
    }

    public Seller(String email, String password, String phoneNo, String fullname, String address, RoleCategory role){
        super(email, password, phoneNo, fullname, address, role);
    }

    //Seller Register Product in Database
    // public Seller(String email, String password, String phoneNo, String fullname, String address, RoleCategory role, List<Product> product){
    //     super(email, password, phoneNo, fullname, address, role);
    //     setProduct(product);
    // }

    public void setSellerId(long seller_id){
        this.seller_id = seller_id;
    }

    public long getSellerId(){
        return seller_id;
    }

    public List<Product> getProduct(){
        return product;
    }

    public void setProduct(List<Product> product){
        this.product = product;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    protected boolean isValidRoleForUser(RoleCategory role){
        return role == RoleCategory.SELLER;
    }
}   
