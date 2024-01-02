package com.foodsense.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foodsense.demo.model.Product;

@Service
public interface ProductService {
    public List<Product> findAllProduct();

    public Product findProductByID(long id);

    public int insertProduct(Product product, long id);

    public int deleteProductByID(long id);

    public int uploadProductImage(Product product, long id);

    public int updateProductInfoByID(Product product, long id);

    public int updateProductStockByID(Product quantity, long id);
}  
