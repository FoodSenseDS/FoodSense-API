package com.foodsense.demo.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.foodsense.demo.model.Customer;
import com.foodsense.demo.service.CustomerService;

@Repository
public class CustomerDAO implements CustomerService{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Customer> findAllCustomer(){
        String query = "SELECT * FROM customer";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Customer.class));
    }

    @Override
    public Customer findCustomerByID(long id){
        String query = "SELECT * FROM customer WHERE customer_id=?";
        return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(Customer.class),id);
    }

    @Override
    public int insertCustomer(Customer customer){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO customer(email,password,phone_number,fullname,address,role) VALUES (?,?,?,?,?,?)";

        int result = jdbcTemplate.update(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, customer.getEmail());
            pstmt.setString(2, customer.getPassword());
            pstmt.setString(3, customer.getphone_number());
            pstmt.setString(4, customer.getFullName());
            pstmt.setString(5, customer.getAddress());
            pstmt.setString(6, customer.getRole().toString());
            return pstmt;
        }, keyHolder);

        if(result > 0){
            Number generatedID = keyHolder.getKey();
            if(generatedID != null){
                return generatedID.intValue();
            }
        }
        return 0;
    }

    @Override
    public int deleteCustomerByID(long id){
        String query = "DELETE FROM customer WHERE customer_id=?";
        return jdbcTemplate.update(query,id);
    }

    @Override
    public int uploadImage(Customer customer, long id){
        String query = "UPDATE customer SET image_url=? WHERE customer_id=?";
        return jdbcTemplate.update(query, customer.getImageUrl(), id);
    }

    @Override
    public int updateCustomerByID(Customer customer, long id){
        final NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        String query = "UPDATE customer SET ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        
        if (customer.getEmail() != null){
            query += "email=:email, ";
            params.addValue("email", customer.getEmail());
        }

        if(customer.getPassword() != null){
            query += "password=:password, ";
            params.addValue("password", customer.getPassword());
        }

        if(customer.getphone_number() != null){
            query += "phone_number=:phone_number, ";
            params.addValue("phone_number", customer.getphone_number());
        }

        if(customer.getFullName() != null){
            query += "fullname=:fullname, ";
            params.addValue("fullname", customer.getFullName());
        }

        if(customer.getAddress() != null){
            query += "address=:address, ";
            params.addValue("address", customer.getAddress());
        }

        if(customer.getRole() != null){
            query += "role=:role, ";
            params.addValue("role", customer.getRole().toString());
        }

        query = query.substring(0, query.lastIndexOf(",")) + " WHERE customer_id=:id";
        params.addValue("id",id);

        return namedParameterJdbcTemplate.update(query,params);
    }
}

