package com.foodsense.demo.model;

import com.foodsense.demo.enumeration.RoleCategory;

public abstract class Auth {
    public abstract void register(String email, String password, String phone_number, String fullName, String address, RoleCategory role);
    
    public abstract void login(String email, String password, RoleCategory role);
}
