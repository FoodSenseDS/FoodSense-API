package com.foodsense.demo.service;

import java.util.List;

import com.foodsense.demo.model.Admin;

public interface AdminService {
    public List<Admin> findAllAdmin();

    public Admin findAdminByID(long id);

    public int insertAdmin(Admin admin);

    public int deleteAdminByID(long id);

    public int updateAdminByID(Admin admin, long id);
}
