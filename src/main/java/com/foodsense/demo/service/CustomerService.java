package com.foodsense.demo.service;

import java.util.List;

import com.foodsense.demo.model.Customer;

public interface CustomerService {
    public List<Customer> findAllCustomer();

    public Customer findCustomerByID(long id);

    public int insertCustomer(Customer customer);

    public int deleteCustomerByID(long id);

    public int uploadImage(Customer customer, long id);
    
    public int updateCustomerByID(Customer customer, long id);
}
