package com.store.resources;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.store.core.Product;
import com.store.db.ProductDAO;

@Path("/store/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class ProductResource {

    private final ProductDAO productDAO;
    // using atomic integer to generate productIDs
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public ProductResource(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    // create a new product
    @Path("/create")
    @POST
    public Response createProduct(
                                  @QueryParam("name") String name,
                                  @QueryParam("description") String description,
                                  @QueryParam("price") double price) {
        
        Integer newID = idCounter.incrementAndGet();
        Product product = new Product();
        product.setId(newID);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        
        productDAO.insert(product.getId(), product.getName(), product.getDescription(), product.getPrice());
        
        return Response.status(Response.Status.CREATED).entity(product).build();
    }
    // retrieve a product by ID
    @GET
    @Path("/id")
    public Response getProduct(@QueryParam("p") int id) {
        Product product = productDAO.findById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(product).build();
    }

    @GET
    public Response getAllProducts() {
        List<Product> products = productDAO.getAllProducts();
        if (products == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(products).build();
    }

    // search for products by name (using query parameter 'search')
    @GET
    @Path("/search")
    public Response searchProducts(@QueryParam("q") String search) {
        List<Product> products;
        if (search == null || search.isEmpty()) {
            // You can choose to return an empty list or all products (if you implement a findAll() method)
            products = List.of();
        } else {
            products = productDAO.searchProductsByName(search);
        }
        return Response.ok(products).build();
    }

    // update a product
    @PUT
    @Path("/update")
    public Response updateProduct(@QueryParam("id") int id,
                                    @QueryParam("name") String name,
                                    @QueryParam("description") String description,
                                    @QueryParam("price") double price) {

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setId(id);

        int rowsUpdated = productDAO.update(product); // Assume update() returns number of rows updated
        if (rowsUpdated == 0) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(product).build();
    }

    @DELETE
    @Path("/delete")
    public Response deleteProduct(@QueryParam("id") int id) {
        int rowsDeleted = productDAO.delete(id); // Assume delete() returns number of rows deleted
        if (rowsDeleted == 0) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
