package com.store.shopping_cart.config;

import javax.sql.DataSource;

import org.jdbi.v3.core.Jdbi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JdbiConfig {
    
    @Bean
    public Jdbi jdbi(DataSource datasource) {
        return Jdbi.create(datasource);
    }
}
