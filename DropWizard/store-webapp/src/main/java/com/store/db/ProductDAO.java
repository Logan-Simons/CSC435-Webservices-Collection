package com.store.db;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;


public interface ProductDAO {
    
    @SqlUpdate("INSERT INTO products (name, price) VALUES (:name, :price)")
    @GetGeneratedKeys
    int insert(@Bind("name") String name, @Bind("price") double price);

}
