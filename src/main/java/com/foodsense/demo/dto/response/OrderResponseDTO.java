package com.foodsense.demo.dto.response;

public class OrderResponseDTO {
    private double price;
    private long orderId;
    private String date;

    public double getPrice(){
        return price;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public long getOrderId(){
        return orderId;
    }

    public void setOrderId(long orderId){
        this.orderId = orderId;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }
}
