package com.foodsense.demo.service;

import com.foodsense.demo.enumeration.RoleCategory;
import com.foodsense.demo.model.User;;

public interface AuthService {
    public int register(String email, String password, String phone_number, String fullName, String address, RoleCategory role);

    public User login(String email, String password, RoleCategory role);
}
