package com.foodsense.demo.service;

import java.util.List;

import com.foodsense.demo.model.Seller;

public interface SellerService {
    public List<Seller> findAllSeller();

    public Seller findSellerByID(long id);

    public int insertSeller(Seller seller);

    public int deleteSellerByID(long id);

    public int uploadImage(Seller seller, long id);

    public int updateSellerByID(Seller seller, long id);
}
