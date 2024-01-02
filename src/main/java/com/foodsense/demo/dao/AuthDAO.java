package com.foodsense.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.foodsense.demo.enumeration.RoleCategory;
import com.foodsense.demo.model.Admin;
import com.foodsense.demo.model.Courier;
import com.foodsense.demo.model.Customer;
import com.foodsense.demo.model.Seller;
import com.foodsense.demo.model.User;
import com.foodsense.demo.service.AuthService;


@Repository
public class AuthDAO implements AuthService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AdminDAO adminDAO;

    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private SellerDAO sellerDAO;

    @Autowired
    private CourierDAO courierDAO;

    public AuthDAO(){}

    @Override
    public int register(String email, String password, String phone_number, String fullName, String address, RoleCategory role){
        if (role == RoleCategory.ADMIN) {
            return adminDAO.insertAdmin(new Admin(email, password, phone_number, fullName, address, role));
        } else if (role == RoleCategory.CUSTOMER) {
            return customerDAO.insertCustomer(new Customer(email, password, phone_number, fullName, address, role));
        } else if (role == RoleCategory.SELLER) {
            return sellerDAO.insertSeller(new Seller(email, password, phone_number, fullName, address, role));
        } else if (role == RoleCategory.COURIER) {
            return courierDAO.insertCourier(new Courier(email, password, phone_number, fullName, address, role));
        }

        return 0;
    }

    @Override
    public User login(String email, String password, RoleCategory role) {
        Object[] params = new Object[] { email, password };
        if (role == RoleCategory.ADMIN) {
            String query = "SELECT * FROM admin WHERE email=? AND password=?";
            return jdbcTemplate.queryForObject(query, params, new BeanPropertyRowMapper<>(Admin.class));
        } else if (role == RoleCategory.CUSTOMER) {
            String query = "SELECT * FROM customer WHERE email=? AND password=?";
            return jdbcTemplate.queryForObject(query, params, new BeanPropertyRowMapper<>(Customer.class));
        } else if (role == RoleCategory.SELLER) {
            String query = "SELECT * FROM seller WHERE email=? AND password=?";
            return jdbcTemplate.queryForObject(query, params, new BeanPropertyRowMapper<>(Seller.class));
        }else if (role == RoleCategory.COURIER) {
            String query = "SELECT * FROM courier WHERE email=? AND password=?";
            return jdbcTemplate.queryForObject(query, params, new BeanPropertyRowMapper<>(Courier.class));
        }
        return null;
    }
}
