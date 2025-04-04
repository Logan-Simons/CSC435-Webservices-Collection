package com.store.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.store.core.Cart;
import com.store.core.Product;
import com.store.db.CartDAO;

@Path("/store/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class CartResource {

    private final CartDAO cartDAO;


    public CartResource(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    public HashMap<Product, Integer> CartProductMapper( List<Map.Entry<Product, Integer>> entries) {
        
        HashMap<Product, Integer> cartProducts = new HashMap<>();
        for (Map.Entry<Product, Integer> entry : entries) {
            cartProducts.put(entry.getKey(), entry.getValue());
        }

        return cartProducts;
    }

@GET
@Path("/")
public Response getCartContents(@QueryParam("id") int id) {
    List<Map.Entry<Product, Integer>> entries = cartDAO.getCartContentsEntries(id);
    HashMap<Product, Integer> cartProducts = CartProductMapper(entries);
    if (cartProducts.isEmpty()) {
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    return Response.status(Response.Status.OK).entity(cartProducts).build(); // use OK (200)
}


@POST
@Path("/create")
public Response createCart() {
    int cartID = cartDAO.newCart();
    Cart cart = new Cart();
    cart.setId(cartID);
    return Response.status(Response.Status.CREATED).entity(cart).build();
}

@GET
@Path("/cost")
public Response cartCost(@QueryParam("cartid") int cartID) {

    double cost = cartDAO.getCartCost(cartID);
    
    return Response.status(Response.Status.OK).entity(cost).build();

}

@POST
@Path("/add")
public Response addProductToCart(@QueryParam("cartid") int cartid, @QueryParam("productid") int productid, @QueryParam("quantity") int quantity) {

    cartDAO.addProductToCart(cartid, productid, quantity);
    
    return Response.status(Response.Status.OK).entity(CartProductMapper(cartDAO.getCartContentsEntries(cartid))).build();
    
}

@PUT
@Path("/sub")
public Response subProductToCart(@QueryParam("cartid") int cartid, @QueryParam("productid") int productid) {

    cartDAO.subtractProductFromCart(cartid, productid);
    
    return Response.status(Response.Status.OK).entity(CartProductMapper(cartDAO.getCartContentsEntries(cartid))).build();
    
}


@DELETE
@Path("/delete")
public Response clearCart(@QueryParam("cartid") int cartid) {

    cartDAO.clearCart(cartid);

    return Response.status(Response.Status.OK).entity("Cleared cart").build();
}
}