package com.foodsense.demo.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.foodsense.demo.model.Courier;
import com.foodsense.demo.service.CourierService;

@Repository
public class CourierDAO implements CourierService{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Courier> findAllCourier(){
        String query = "SELECT * FROM courier";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Courier.class));
    }

    @Override
    public Courier findCourierByID(long id){
        String query = "SELECT * FROM courier WHERE courier_id=?";
        return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(Courier.class), id);
    }

    @Override
    public int insertCourier(Courier courier){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query="INSERT INTO courier (email,password,phone_number,fullname,address,role,on_delivery) VALUES(?,?,?,?,?,?,?)";

        int result = jdbcTemplate.update(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, courier.getEmail());
            pstmt.setString(2, courier.getPassword());
            pstmt.setString(3, courier.getphone_number());
            pstmt.setString(4, courier.getFullName());
            pstmt.setString(5, courier.getAddress());
            pstmt.setString(6, courier.getRole().toString());
            pstmt.setBoolean(7, courier.getOnDelivery());
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
    public int deleteCourierByID(long id){
        String query = "DELETE FROM courier WHERE courier_id=?";
        return jdbcTemplate.update(query, id);
    }

    @Override
    public int uploadImage(Courier courier, long id){
        String query = "UPDATE courier SET image_url=? WHERE courier_id=?";
        return jdbcTemplate.update(query, courier.getImageUrl(), id);
    }

    @Override
    public int updateCourierByID(Courier courier, long id){
        final NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        String query = "UPDATE courier SET ";

        MapSqlParameterSource params = new MapSqlParameterSource();

        if (courier.getEmail() != null){
            query += "email=:email, ";
            params.addValue("email", courier.getEmail());
        }

        if(courier.getPassword() != null){
            query += "password=:password, ";
            params.addValue("password", courier.getPassword());
        }

        if(courier.getphone_number() != null){
            query += "phone_number=:phone_number, ";
            params.addValue("phone_number", courier.getphone_number());
        }

        if(courier.getFullName() != null){
            query += "fullname=:fullname, ";
            params.addValue("fullname", courier.getFullName());
        }

        if(courier.getAddress() != null){
            query += "address=:address, ";
            params.addValue("address", courier.getAddress());
        }

        if(courier.getRole() != null){
            query += "role=:role, ";
            params.addValue("role", courier.getRole().toString());
        }

        if(courier.getOnDelivery() != false){
            query += "on_delivery=:on_delivery, ";
            params.addValue("on_delivery", courier.getOnDelivery());
        }

        if(courier.getOnDelivery() != true){
            query += "on_delivery=:on_delivery, ";
            params.addValue("on_delivery", false);
        }

        query = query.substring(0, query.lastIndexOf(",")) + " WHERE courier_id=:id";
        params.addValue("id",id);

        return namedParameterJdbcTemplate.update(query, params);
    }
}
