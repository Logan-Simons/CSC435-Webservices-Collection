package com.store.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import io.dropwizard.jdbi3.HandleProvider;
import io.dropwizard.jdbi3.JdbiFactory;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.Handle;
import com.store.db.CartDAO;

@Path("/hello")
@Produces(MediaType.TEXT_PLAIN)
public class CartResource {
    private final CartDAO dao;
    private final HandleProvider handleProvider;

    public CartResource(CartDAO dao, HandleProvider handleProvider) {
        this.dao = dao;
        this.handleProvider = handleProvider;
    }

    @GET
    public String sayHello() {
        return "Hello, World!";
    }
}