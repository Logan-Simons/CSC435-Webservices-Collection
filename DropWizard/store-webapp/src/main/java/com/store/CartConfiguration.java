package com.store;

import io.dropwizard.Configuration;
import io.dropwizard.server.DefaultServerFactory;

public class CartConfiguration extends Configuration {
    // This constructor ensures we have a server factory configured
    public CartConfiguration() {
        // Set a default server factory if none provided in YAML
        setServerFactory(new DefaultServerFactory());
    }
}