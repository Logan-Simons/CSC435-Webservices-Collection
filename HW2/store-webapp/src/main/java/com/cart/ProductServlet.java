package com.cart;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    ArrayList<Product> listOfProducts = new ArrayList<>();

    private void createProducts() {

    Product product1 = new Product(1, "Item 1", 3.99);
    Product product2 = new Product(2, "Item 2", 4.99);
    Product product3 = new Product(3, "Item 3", 5.99);
    listOfProducts.add(product1);
    listOfProducts.add(product2);
    listOfProducts.add(product3);

    }

    


    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print("{\"message\": \"these are my product\"}");
        out.flush();

    }

    // @GET
    // /products
    // returns the list of products, including ID, name, and cost

    // @POST
    // /products/create/name=?/price=?
    // creates a product stored locally in the servlet, returns the ID of the product

    // @PUT
    // /products/update/id=?/price=?
    // finds and updates the price of the product based on the id and updated cost

    // @DELETE
    // /products/delete/id=?
    // finds the product based on the ID, and deletes it
    
}
