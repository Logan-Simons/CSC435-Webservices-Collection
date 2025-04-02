package com.store;

import org.jdbi.v3.core.Jdbi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.db.ProductDAO;
import com.store.resources.ProductResource;

import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class StoreApplication extends Application<StoreConfiguration> {

    public static void main(String[] args) throws Exception {
        new StoreApplication().run(args);
    }

    @Override
    public String getName() {
        return "cart-dropwizard";
    }

    @Override
    public void initialize(Bootstrap<StoreConfiguration> bootstrap) {
       
       

        // Register HttpConnectorFactory with the ObjectMapper
        ObjectMapper mapper = bootstrap.getObjectMapper();
        // Register the connector factory
        mapper.registerSubtypes(HttpConnectorFactory.class);
    }

  @Override
public void run(StoreConfiguration configuration, Environment environment) {
    final JdbiFactory factory = new JdbiFactory();
    final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
    final ProductDAO dao = jdbi.onDemand(ProductDAO.class);
    
    // Register the resource
    final ProductResource resource = new ProductResource(dao);
    environment.jersey().register(resource);
}
}