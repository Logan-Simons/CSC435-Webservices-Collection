package com.store;

import org.jdbi.v3.core.Jdbi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.db.CartDAO;
import com.store.resources.CartResource;

import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.store.resources.CartResource;

public class CartApplication extends Application<CartConfiguration> {

    public static void main(String[] args) throws Exception {
        new CartApplication().run(args);
    }

    @Override
    public String getName() {
        return "cart-dropwizard";
    }

    @Override
    public void initialize(Bootstrap<CartConfiguration> bootstrap) {
       
       

        // Register HttpConnectorFactory with the ObjectMapper
        ObjectMapper mapper = bootstrap.getObjectMapper();
        // Register the connector factory
        mapper.registerSubtypes(HttpConnectorFactory.class);
    }

    @Override
    public void run(CartConfiguration configuration, Environment environment) {
        
        final JdbiFactory factory = new JdbiFactory();
    final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
    final CartDAO dao = jdbi.onDemand(CartDAO.class);
        
        // Register the resource
        final CartResource resource = new CartResource(dao, new HandleProvider(jdbi));
        environment.jersey().register(resource);
    }
}