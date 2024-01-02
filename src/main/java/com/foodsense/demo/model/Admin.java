package com.foodsense.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.foodsense.demo.enumeration.RoleCategory;

import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity
@Table(name="admin")
public class Admin extends User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="admin_id")
    private long admin_id;

    public Admin(){}

    public Admin(long admin_id, String email, String password, String phoneNo, String fullname, String address, RoleCategory role){
        super(email, password, phoneNo, fullname, address, role);
        setAdminId(admin_id);
    }

    public Admin(String email, String password, String phoneNo, String fullname, String address, RoleCategory role){
        super(email, password, phoneNo, fullname, address, role);
    }

    public long getAdminId(){
        return admin_id;
    }

    public void setAdminId(long admin_id){
        this.admin_id = admin_id;
    }

    @Override
    protected boolean isValidRoleForUser(RoleCategory role){
        return role == RoleCategory.ADMIN;
    }
}
