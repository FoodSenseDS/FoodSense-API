package com.foodsense.demo.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.foodsense.demo.model.Seller;
import com.foodsense.demo.service.SellerService;

@Repository
public class SellerDAO implements SellerService{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Seller> findAllSeller(){
        String query = "SELECT * FROM seller";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Seller.class));
    }

    @Override
    public Seller findSellerByID(long id){
        String query = "SELECT * FROM seller WHERE seller_id=?";
        return jdbcTemplate.queryForObject(query,new BeanPropertyRowMapper<>(Seller.class),id);
    }

    @Override
    public int insertSeller(Seller seller){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query="INSERT INTO seller(email,password,phone_number,fullname,address,role) VALUES(?,?,?,?,?,?)";

        int result = jdbcTemplate.update(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, seller.getEmail());
            pstmt.setString(2, seller.getPassword());
            pstmt.setString(3, seller.getphone_number());
            pstmt.setString(4, seller.getFullName());
            pstmt.setString(5, seller.getAddress());
            pstmt.setString(6, seller.getRole().toString());
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
    public int deleteSellerByID(long id){
        String query = "DELETE FROM seller WHERE seller_id=?";
        return jdbcTemplate.update(query,id);
    }

    @Override
    public int uploadImage(Seller seller, long id){
        String query = "UPDATE seller SET image_url=? WHERE seller_id=?";
        return jdbcTemplate.update(query,seller.getImageUrl(),id);
    }

    @Override
    public int updateSellerByID(Seller seller, long id){
        final NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        String query = "UPDATE seller SET ";

        MapSqlParameterSource params = new MapSqlParameterSource();

        if(seller.getEmail() != null){
            query += " email=:email,";
            params.addValue("email", seller.getEmail());
        }

        if(seller.getPassword() != null){
            query += "password=:password, ";
            params.addValue("password", seller.getPassword());
        }

        if(seller.getphone_number() != null){
            query += "phone_number=:phone_number, ";
            params.addValue("phone_number", seller.getphone_number());
        }

        if(seller.getFullName() != null){
            query += "fullname=:fullname, ";
            params.addValue("fullname", seller.getFullName());
        }

        if(seller.getAddress() != null){
            query += "address=:address, ";
            params.addValue("address", seller.getAddress());
        }

        if(seller.getRole() != null){
            query += "role=:role, ";
            params.addValue("role", seller.getRole().toString());
        }

        query = query.substring(0, query.lastIndexOf(",")) + " WHERE seller_id=:id";
        params.addValue("id",id);

        return namedParameterJdbcTemplate.update(query,params);
    }
}
