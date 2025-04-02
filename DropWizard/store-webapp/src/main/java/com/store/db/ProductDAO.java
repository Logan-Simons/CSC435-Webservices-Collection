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
    
    @SqlUpdate("INSERT INTO products (name, price) VALUES (:name, :price)")
    @GetGeneratedKeys
    int insert(@Bind("name") String name, @Bind("price") double price);

    @SqlQuery("SELECT * FROM WHERE name ILIKE '%' || :search || '%'")
    @RegisterBeanMapper(Product.class)
    List<Product> searchProductsByName(@Bind("search") String search);

    @SqlQuery("SELECT * FROM products WHERE id = :id")
    @RegisterBeanMapper(Product.class)
    Product findById(@Bind("id") int id);

    @SqlQuery("SELECT * FROM products")
    @RegisterBeanMapper(Product.class)
    List<Product> getAllProducts();

    @SqlUpdate("UPDATE products SET name = :name, price = :price WHERE id = :id")
    int update(@BindBean Product product);

    @SqlUpdate("DELETE FROM products WHERE id = :id")
    int delete(@Bind("id") int id);


}
