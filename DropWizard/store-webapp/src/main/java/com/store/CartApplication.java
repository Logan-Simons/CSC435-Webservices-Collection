package com.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.resources.CartResource;

import io.dropwizard.Application;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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
        // Register the resource
        final CartResource resource = new CartResource();
        environment.jersey().register(resource);
    }
}