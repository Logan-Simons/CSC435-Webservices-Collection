package com.store;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.store.Cart;
import com.store.Product;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    com.store.Cart Cart = new Cart("cart-1");
    ArrayList<Product> listOfProducts = new ArrayList<>();

    public CartServlet() {
        createProducts();
        Cart.setProducts(listOfProducts);
    }

    // Simulated shopping cart
    private void createProducts() {

        Product product1 = new Product(1, "Item 1", 3.99);
        Product product2 = new Product(2, "Item 2", 4.99);
        Product product3 = new Product(3, "Item 3", 5.99);
        listOfProducts.add(product1);
        listOfProducts.add(product2);
        listOfProducts.add(product3);

    }

    // @GET /cart
    // returns items in user cart
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String servletPath = req.getServletPath();

        if ("/cart".equals(servletPath)) {
            System.out.println(servletPath);
            handleGetCart(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", "invalid method");
            writeResponse(resp, errorJson);
        }

    }

    private void handleGetCart(HttpServletRequest req, HttpServletResponse resp) {

        JSONArray cartArrayJson = new JSONArray();
        JSONObject responseJson = new JSONObject();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        for (int i = 0; i < Cart.getCartSize(); i++) {
            JSONObject productObjectJson = new JSONObject();
            productObjectJson.put("cart-index", i);
            productObjectJson.put("name", Cart.getProductByIndex(i).getProductName());
            productObjectJson.put("price", Cart.getProductByIndex(i).getProductCost().toString);
            productObjectJson.put("quantity", Cart.getProductIndexQuantity(i).toString);
            cartArrayJson.put(productObjectJson);
        }

        responseJson.put("cart-contents", cartArrayJson);
        PrintWriter out = resp.getWriter();
        writeResponse(resp, responseJson);

    }

    // @POST /cart/add/id=?/
    // adds items in user cart
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        String servletPath = req.getServletPath();

        if ("/cart/add".equals(servletPath)) {
            System.out.println(servletPath);
            handleAddToCart(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", "invalid method");
            writeResponse(resp, errorJson);
        }

    }

    private void handleAddToCart(HttpServletRequest req, HttpServletResponse resp) {

        int productID = req.getParameter("id");
        if (listOfProducts.get(productID) != null) {
            Cart.addProduct(productID);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", "invalid product index");
            writeResponse(resp, errorJson);
        }

        ArrayList<Product> newCart = Cart.getProducts();
        JSONArray productArray = new JSONArray();

        for (int i = 0; i < newCart.size(); i++) {
            JSONObject product = new JSONObject();

            product.put("name", Cart.getProductByIndex(i).getProductName);
            product.put("price", Double.parseDouble(Cart.getProductByIndex(i).getProductCost));
            product.put("quantity", (Cart.getProductIndexQuantity(i)));
            productArray.put(product);
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        JSONObject responseJson = new JSONObject();
        responseJson.put(productArray);
        writeResponse(resp, responseJson);

    }

    private void writeResponse(HttpServletResponse resp, JSONObject responseJson) {
        try (PrintWriter out = resp.getWriter()) {
            out.print(responseJson.toString());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // @DELETE /cart/clear
}
