package com.store.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.store.core.Product;
import com.store.db.ProductDAO;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    private final ProductDAO productDAO;

    public ProductResource(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    // Create a new product
    @POST
    public Response createProduct(Product product) {
        // Inserts the product and returns the generated ID.
        int newId = productDAO.insert(product.getName(), product.getPrice());
        product.setId(newId);
        return Response.status(Response.Status.CREATED).entity(product).build();
    }

    // Retrieve a product by ID
    @GET
    @Path("/{id}")
    public Response getProduct(@PathParam("id") int id) {
        Product product = productDAO.findById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(product).build();
    }

    // Search for products by name (using query parameter 'search')
    @GET
    public Response searchProducts(@QueryParam("search") String search) {
        List<Product> products;
        if (search == null || search.isEmpty()) {
            // You can choose to return an empty list or all products (if you implement a findAll() method)
            products = List.of();
        } else {
            products = productDAO.searchProductsByName(search);
        }
        return Response.ok(products).build();
    }

    // Update a product (requires that you implement the update method in ProductDAO)
    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") int id, Product product) {
        product.setId(id);
        int rowsUpdated = productDAO.update(product); // Assume update() returns number of rows updated
        if (rowsUpdated == 0) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(product).build();
    }

    // Delete a product (requires that you implement the delete method in ProductDAO)
    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") int id) {
        int rowsDeleted = productDAO.delete(id); // Assume delete() returns number of rows deleted
        if (rowsDeleted == 0) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
