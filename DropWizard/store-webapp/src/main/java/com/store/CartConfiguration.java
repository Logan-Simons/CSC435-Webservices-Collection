package com.store;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.server.DefaultServerFactory;

public class CartConfiguration extends Configuration {
   
        @Valid
        @NotNull
        private DataSourceFactory database = new DataSourceFactory();

        @JsonProperty("database")
         public DataSourceFactory getDataSourceFactory() {
        return database;
         }

        @JsonProperty("database")
        public void setDataSourceFactory(DataSourceFactory factory) {
            this.database = factory;
        }
   
    // This constructor ensures we have a server factory configured
    public CartConfiguration() {

        // Set a default server factory if none provided in YAML
        setServerFactory(new DefaultServerFactory());
    }
}