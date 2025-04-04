package com.store.db;

import java.util.List;

import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import com.store.core.Product;



public interface ProductDAO extends SqlObject {
    
    @SqlUpdate("INSERT INTO product (productid, name, description, price) VALUES (:productid, :name, :description, :price)")
    @GetGeneratedKeys
    int insert(@Bind("productid") int productid, @Bind("name") String name, @Bind("description") String description, @Bind("price") double price);

    @SqlQuery("SELECT productid AS id, name, description, price FROM product WHERE name ILIKE '%' || :search || '%'")
    @RegisterBeanMapper(Product.class)
    List<Product> searchProductsByName(@Bind("search") String search);


    @SqlQuery("SELECT productid AS id, name, description, price FROM product WHERE productid = :productid")
    @RegisterBeanMapper(Product.class)
    Product findById(@Bind("productid") int productid);

    @SqlQuery("SELECT productid AS id, name, description, price FROM product")
    @RegisterBeanMapper(Product.class)
    List<Product> getAllProducts();

    @SqlUpdate("UPDATE product SET name = :name, description = :description, price = :price WHERE productid = :id")
    int update(@BindBean Product product);

    @SqlUpdate("DELETE FROM product WHERE productid = :productid")
    int delete(@Bind("productid") int productid);


}
