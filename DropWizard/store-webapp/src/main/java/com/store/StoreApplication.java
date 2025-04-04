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
        ObjectMapper mapper = bootstrap.getObjectMapper();
        mapper.registerSubtypes(HttpConnectorFactory.class);
    }

    @Override
    public void run(StoreConfiguration configuration, Environment environment) {
        // connect to database with JDBI
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        // initialize connection to our DAO classes with JDBI
        final ProductDAO productDAO = jdbi.onDemand(ProductDAO.class);
        final CartDAO cartDAO = jdbi.onDemand(CartDAO.class);
        // initialize resources
        final ProductResource productResource = new ProductResource(productDAO);
        final CartResource cartResource = new CartResource(cartDAO);
        // register our web-service resources
        environment.jersey().register(productResource);
        environment.jersey().register(cartResource);
    }
}