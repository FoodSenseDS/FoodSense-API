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

import com.foodsense.demo.model.Product;
import com.foodsense.demo.model.Seller;
import com.foodsense.demo.repository.ProductJPARepo;
import com.foodsense.demo.service.ProductService;

@Repository
public class ProductDAO implements ProductService{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SellerDAO sellerDAO;

    @Autowired
    private ProductJPARepo productJPARepo;

    @Override
    public List<Product> findAllProduct(){
        return productJPARepo.findAll();
    }

    @Override
    public Product findProductByID(long id){
        return productJPARepo.getReferenceById(id);
    }

    @Override
    public int insertProduct(Product product, long id){
        Seller seller = sellerDAO.findSellerByID(id);
        product.setSeller(seller);
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO product(product_name,product_stock,product_unit_price,product_description,product_is_food,seller_id) VALUES (?,?,?,?,?,?)";
        
        int result = jdbcTemplate.update(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, product.getProductName());
            pstmt.setInt(2, product.getProductStock());
            pstmt.setDouble(3, product.getProductUnitPrice());
            pstmt.setString(4, product.getProductDescription());
            pstmt.setBoolean(5, product.getIsFood());
            pstmt.setLong(6, product.getSeller().getSellerId());
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
    public int deleteProductByID(long id){
        String query = "DELETE FROM product WHERE product_id=?";
        return jdbcTemplate.update(query, id);
    }
    
    @Override
    public int uploadProductImage(Product product, long id){
        String query = "UPDATE product SET image_url=? WHERE product_id=?";
        return jdbcTemplate.update(query, product.getImageUrl(), id);
    }

    @Override
    public int updateProductInfoByID(Product product, long id){
        final NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        String query = "UPDATE product SET ";

        MapSqlParameterSource params = new MapSqlParameterSource();

        if(product.getProductName() != null){
            query += "product_name=:product_name, ";
            params.addValue("product_name", product.getProductName());
        }

        if(product.getProductStock() > 0){
            query += "product_stock=:product_stock, ";
            params.addValue("product_stock", product.getProductStock());
        }

        if(product.getProductUnitPrice() > 0){
            query += "product_unit_price=:product_unit_price, ";
            params.addValue("product_unit_price", product.getProductUnitPrice());
        }

        if(product.getProductDescription() != null){
            query += "product_description=:product_description";
            params.addValue("product_description", product.getProductDescription());
        }

        if(product.getIsFood() != false){
            query += "product_is_food=:product_is_food, ";
            params.addValue("product_is_food", true);
        }

        if(product.getIsFood() != true){
            query += "product_is_food=:product_is_food, ";
            params.addValue("product_is_food", false);
        }

        query = query.substring(0, query.lastIndexOf(","));
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(query, params);
    }

    @Override
    public int updateProductStockByID(Product quantity, long id){
        String selectStockQuery = "SELECT product_stock FROM product WHERE product_id=?";
        Product currentStock = jdbcTemplate.queryForObject(selectStockQuery, new BeanPropertyRowMapper<>(Product.class), id);

        if (currentStock != null) {
            currentStock.updateProductStock(quantity.getProductStock());

            String updateStockQuery = "UPDATE product SET product_stock=? WHERE product_id=?";
            return jdbcTemplate.update(updateStockQuery, currentStock.getProductStock(), id);
        }
        
        return 0;
    }
}
