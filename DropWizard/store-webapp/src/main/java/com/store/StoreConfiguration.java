package com.store;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.server.DefaultServerFactory;

public class StoreConfiguration extends Configuration {
   
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
   
    public StoreConfiguration() {
        
        setServerFactory(new DefaultServerFactory());
    }
}