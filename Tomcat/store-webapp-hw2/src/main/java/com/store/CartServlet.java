package com.store;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/cart/*")
public class CartServlet extends HttpServlet {

    private Cart cart = new Cart("cart-1");
    private HashMap<Integer, Product> productMap = new HashMap<>();

    // Initialize Product Data and Cart
    public CartServlet() {
        ProductData data = new ProductData();
        productMap = data.getInitialProducts();
        initializeCart();
    }

    private void initializeCart() {
        HashMap<Product, Integer> initialCart = new HashMap<>();
        cart.setProducts(initialCart);
    }

    // @GET
    // GET request filter
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            handleGetCart(req, resp);
        } else if (pathInfo.equals("/cost")) {
            handleCartCost(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // Handle @GET /cart request
    private void handleGetCart(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        JSONArray cartArrayJson = new JSONArray();

        int index = 0;
        for (Product product : cart.getProducts().keySet()) {
            if (cart.getProductQuantity(product) > 0) {
                JSONObject productObjectJson = new JSONObject();
                productObjectJson.put("cart-index", index++);
                productObjectJson.put("id", product.getProductID());
                productObjectJson.put("name", product.getProductName());
                productObjectJson.put("price", product.getProductCost());
                productObjectJson.put("quantity", cart.getProductQuantity(product));
                cartArrayJson.put(productObjectJson);
            }
        }

        JSONObject responseJson = new JSONObject();
        responseJson.put("cart-contents", cartArrayJson);
        writeResponse(resp, responseJson);
    }

    // Handle @GET /cart/cost request
    private void handleCartCost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        JSONObject costJson = new JSONObject();
        costJson.put("cost", cart.getCartCost()); // Adjusted to match parameterless method
        costJson.put("total-items", cart.getTotalItems());

        writeResponse(resp, costJson);
    }

    // @POST
    // POST request Filter
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String servletString = req.getPathInfo();
        if (servletString != null && servletString.startsWith("/add")) {
            handleAddProducts(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    // Handle POST /cart/add
    private void handleAddProducts(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        JSONObject responseJson = new JSONObject();

        try {
            int productID = Integer.parseInt(req.getParameter("product-id"));
            int modifyQuantity = Integer.parseInt(req.getParameter("quantity"));

            Product product = productMap.get(productID);
            if (product != null) {
                if (modifyQuantity > 0) {
                    for (int i = 0; i < modifyQuantity; i++) {
                        cart.addProduct(product);
                    }
                    int currentQuantity = cart.getProductQuantity(product);
                    responseJson.put("success", "product " + product.getProductName() +
                            " now has quantity " + currentQuantity);
                    responseJson.put("quantity", currentQuantity);
                } else if (modifyQuantity < 0 && cart.getProductQuantity(product) > 0) {
                    for (int i = 0; i < -modifyQuantity; i++) {
                        cart.removeProduct(product);
                    }
                    int currentQuantity = cart.getProductQuantity(product);
                    responseJson.put("success", "product " + product.getProductName() +
                            " now has quantity " + currentQuantity);
                    responseJson.put("quantity", currentQuantity);
                }
            } else {
                responseJson.put("error", "Invalid product ID");
            }
        } catch (NumberFormatException e) {
            responseJson.put("error", "Invalid quantity or product ID format");
        }

        writeResponse(resp, responseJson);
    }

    // @DELETE
    // DELETE request filter
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.equals("/clear")) {
            handleClearCart(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    // @DELETE cart/clear request handler
    private void handleClearCart(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        JSONObject responseJson = new JSONObject();

        HashMap<Product, Integer> emptyCart = new HashMap<>();
        for (Product p : productMap.values()) {
            emptyCart.put(p, 0);
        }
        cart.setProducts(emptyCart);

        if (cart.getTotalItems() == 0) {
            responseJson.put("message", "success! cart cleared.");
        } else {
            responseJson.put("message", "error clearing cart");
        }
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
}