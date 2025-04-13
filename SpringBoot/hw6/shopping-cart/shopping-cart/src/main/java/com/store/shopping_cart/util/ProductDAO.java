package com.store.shopping_cart.util;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import com.store.shopping_cart.model.Product;


@RegisterConstructorMapper(Product.class)
public interface ProductDAO {


    @SqlUpdate("INSERT INTO product (name, description, price) VALUES (:name, :description, :price)")
    void insertProduct(String name, String description, double price);

    @SqlQuery("SELECT * FROM product WHERE productid = :productid")
    Product findProductById(int productid);

    @SqlQuery("SELECT * FROM product")
    List<Product> getAllProducts();

    @SqlUpdate("UPDATE product SET name = :name, description = :description, price = :price WHERE productid = :productid")
    void updateProduct(int productid, String name, String description, double price);

    @SqlUpdate("DELETE FROM product WHERE productid = :productid")
    void deleteProduct(int productid);
    
    
}


