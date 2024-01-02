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

import com.foodsense.demo.model.Admin;
import com.foodsense.demo.service.AdminService;

@Repository
public class AdminDAO implements AdminService{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Admin> findAllAdmin() {
        String query = "SELECT * FROM admin";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Admin.class));
    }

    @Override
    public Admin findAdminByID(long id){
        String query = "SELECT * FROM admin WHERE admin_id=?";
        return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(Admin.class), id);
    }

    @Override
    public int insertAdmin(Admin admin){
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String query = "INSERT INTO admin (email,password,phone_number,fullname,address,role) VALUES(?,?,?,?,?,?)";
        int result = jdbcTemplate.update(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, admin.getEmail());
            pstmt.setString(2, admin.getPassword());
            pstmt.setString(3, admin.getphone_number());
            pstmt.setString(4, admin.getFullName());
            pstmt.setString(5, admin.getAddress());
            pstmt.setString(6, admin.getRole().toString());
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
    public int deleteAdminByID(long id){
        String query = "DELETE FROM admin WHERE admin_id=?";
        return jdbcTemplate.update(query, id);
    }

    @Override
    public int updateAdminByID(Admin admin, long id){
        final NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        String query = "UPDATE admin SET ";

        MapSqlParameterSource params = new MapSqlParameterSource();

        if (admin.getEmail() != null){
            query += "email=:email, ";
            params.addValue("email", admin.getEmail());
        }

        if(admin.getPassword() != null){
            query += "password=:password, ";
            params.addValue("password", admin.getPassword());
        }

        if(admin.getphone_number() != null){
            query += "phone_number=:phone_number, ";
            params.addValue("phone_number", admin.getphone_number());
        }

        if(admin.getFullName() != null){
            query += "fullname=:fullname, ";
            params.addValue("fullname", admin.getFullName());
        }

        if(admin.getAddress() != null){
            query += "address=:address, ";
            params.addValue("address", admin.getAddress());
        }

        if(admin.getRole() != null){
            query += "role=:role, ";
            params.addValue("role", admin.getRole().toString());
        }

        query = query.substring(0, query.lastIndexOf(",")) + " WHERE admin_id=:id";
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(query, params);
    }
}
