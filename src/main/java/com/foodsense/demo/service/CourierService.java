package com.foodsense.demo.service;

import java.util.List;

import com.foodsense.demo.model.Courier;

public interface CourierService {
    public List<Courier> findAllCourier();

    public Courier findCourierByID(long id);

    public int insertCourier(Courier courier);

    public int deleteCourierByID(long id);

    public int uploadImage(Courier courier, long id);

    public int updateCourierByID(Courier courier, long id);
}
