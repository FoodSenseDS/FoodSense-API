package com.foodsense.demo.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotEmpty;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import com.foodsense.demo.enumeration.RoleCategory;

@MappedSuperclass
public class User extends Auth{

    @Column(name="email")
    @NotEmpty(message="This attribute may not empty")
    private String email;

    @Column(name="password")
    @NotEmpty(message="This attribute may not empty")
    private String password;

    @Column(name="phone_number")
    @NotEmpty(message="This attribute may not empty")
    private String phone_number;    

    @Column(name="fullname")
    @NotEmpty(message="This attribute may not empty")
    private String fullName;

    @Column(name="address")
    @NotEmpty(message="This attribute may not empty")
    private String address;

    @Column(name="role")
    @NotEmpty(message="This attribute may not empty")
    @Enumerated(EnumType.STRING)
    private RoleCategory role;

    public User(){}

    public User(String email, String fullName){
        setEmail(email);
        setFullName(fullName);
    }

    public User(String email, String password, String phone_number, String fullName, String address, RoleCategory role){
        setEmail(email);
        setPassword(password);
        setphone_number(phone_number);
        setFullName(fullName);
        setAddress(address);
        setRole(role);
    }
    
    @Override
    public void register(String email, String password, String phone_number, String fullName, String address, RoleCategory role){
        setEmail(email);
        setPassword(password);
        setphone_number(phone_number);
        setFullName(fullName);
        setAddress(address);

        if(isValidRoleForUser(role)) {
            setRole(role);
            System.out.println("Registration successfull for user role: " + role.toString());
        } else {
            System.out.println("Invalid role for registration");
        }
    }
    
    @Override
    public void login(String email, String password, RoleCategory role){
        if (this.getEmail().equals(email) && this.getPassword().equals(password)){
            System.out.println("Login Successful");
        } else {
            System.out.println("Invalid Credentials");
        }
    }

    // public abstract void displayInfo();

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getphone_number(){
        return phone_number;
    }

    public void setphone_number(String phone_number){
        this.phone_number = phone_number;
    }

    public String getFullName(){
        return fullName;
    }

    public void setFullName(String fullName){
        this.fullName = fullName;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public RoleCategory getRole(){
        return role;
    }


    public void setRole(RoleCategory role){
        this.role = role;
    }

    protected boolean isValidRoleForUser(RoleCategory role){
        return role == RoleCategory.ADMIN || role == RoleCategory.CUSTOMER || role == RoleCategory.SELLER || role == RoleCategory.COURIER;
    }
}