package com.store;

import org.jdbi.v3.core.Jdbi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.db.CartDAO;
import com.store.db.ProductDAO;
import com.store.resources.CartResource;
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
        return "store-dropwizard";
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
        final ProductDAO productDAO = jdbi.onDemand(ProductDAO.class);
        final CartDAO cartDAO = jdbi.onDemand(CartDAO.class);
        // Register the resource
        final ProductResource productResource = new ProductResource(productDAO);
        final CartResource cartResource = new CartResource(cartDAO);

        environment.jersey().register(productResource);
        environment.jersey().register(cartResource);
    }
}