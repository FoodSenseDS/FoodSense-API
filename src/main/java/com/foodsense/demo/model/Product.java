package com.foodsense.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name="product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private long productId;

    @Column(name="product_name")
    @NotEmpty(message="This attribute may not empty")
    private String productName;

    @Column(name="product_stock")
    @NotNull(message="This attribute may not null")
    private int productStock;

    @Column(name="product_unit_price")
    @NotNull(message="This attribute may not null")
    private double productUnitPrice;

    @Column(name="product_description")
    @NotEmpty(message="This attribute may not empty")
    private String productDescription;

    @Column(name="product_is_food")
    @NotNull(message="This attribute may not be null")
    private boolean isFood;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="seller_id")
    @JsonIgnoreProperties("product")
    private Seller seller;

    @Column(name="image_url")
    private String imageUrl;

    public Product(){}

    public Product(long productId, String productName, int productStock, double productUnitPrice, boolean isFood, String productDescription, String imageUrl, Seller seller){
        setProductId(productId);
        setProductName(productName);
        setProductStock(productStock);
        setProductUnitPrice(productUnitPrice);
        setIsFood(isFood);
        setProductDescription(productDescription);
        setImageUrl(imageUrl);
        setSeller(seller);
    }

    public Product(String productName, int productStock, double productUnitPrice, boolean isFood, String productDescription, String imageUrl){
        setProductName(productName);
        setProductStock(productStock);
        setProductUnitPrice(productUnitPrice);
        setIsFood(isFood);
        setProductDescription(productDescription);
        setImageUrl(imageUrl);
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

    public int getProductStock(){
        return productStock;
    }

    public void setProductStock(int productStock){
        this.productStock = productStock;
    }

    public double getProductUnitPrice(){
        return productUnitPrice;
    }

    public void setProductUnitPrice(double productUnitPrice){
        this.productUnitPrice = productUnitPrice;
    }

    public boolean getIsFood(){
        return isFood;
    }

    public void setIsFood(boolean isFood){
        this.isFood = isFood;
    }

    public String getProductDescription(){
        return productDescription;
    }

    public void setProductDescription(String productDescription){
        this.productDescription = productDescription;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public Seller getSeller(){
        return seller;
    }

    public void setSeller(Seller seller){
        this.seller = seller;
    }

    public void showSellerInfo(Seller seller){
        System.out.println("Seller ID   : " + seller.getSellerId());  
        System.out.println("Full Name   : " + seller.getFullName()); 
        System.out.println("Phone Number: " + seller.getphone_number());
        System.out.println("Address     : " + seller.getAddress());
    }

    public void updateProductStock(int quantity){
        this.productStock +=  quantity;
    }
}
