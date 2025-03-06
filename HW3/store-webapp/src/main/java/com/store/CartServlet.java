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

    // private Cart cart = new Cart("cart-1");
    private HashMap<Integer, Product> productMap = new HashMap<>();
    private CartUtil cartUtil = new CartUtil();
    ProductUtil productUtil = new ProductUtil();

    // Initialize Product Data and Cart
    public CartServlet() {

        refreshProducts();
    }

    public void refreshProducts() {

        productMap = productUtil.getProducts();
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

    // @GET get card
    private void handleGetCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // or retrieve dynamically from session/request
        String cartID = req.getParameter("id").toString();
        CartUtil cartUtil = new CartUtil();
        HashMap<Product, Integer> cartContents = cartUtil.getCartContents(cartID);

        JSONArray cartArrayJson = new JSONArray();
        for (Product product : cartContents.keySet()) {
            JSONObject productObjectJson = new JSONObject();
            productObjectJson.put("id", product.getProductID());
            productObjectJson.put("name", product.getProductName());
            productObjectJson.put("price", product.getProductCost());
            productObjectJson.put("quantity", cartContents.get(product));
            cartArrayJson.put(productObjectJson);
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put("cart-contents", cartArrayJson);
        writeResponse(resp, responseJson);
    }

    // Handle @GET /cart/cost request
    private void handleCartCost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String cartID = req.getParameter("id").toString();
        JSONObject costJson = new JSONObject();
        costJson.put("cost", cartUtil.getCartCost(cartID)); // Adjusted to match parameterless method
        costJson.put("total-items", cartUtil.getCartSize(cartID));

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
            String cartID = req.getParameter("id").toString();
            int productID = Integer.parseInt(req.getParameter("product-id"));
            int modifyQuantity = Integer.parseInt(req.getParameter("quantity"));

            Product product = productMap.get(productID);
            if (product != null) {
                if (modifyQuantity > 0) {
                    for (int i = 0; i < modifyQuantity; i++) {
                        cartUtil.addProductToCart(cartID, product, modifyQuantity);
                    }
                    int currentQuantity = cartUtil.getCartContents(cartID).get(product);
                    responseJson.put("success", "product " + product.getProductName() +
                            " now has quantity " + currentQuantity);
                    responseJson.put("quantity", currentQuantity);
                } else if (modifyQuantity < 0 && cartUtil.getCartContents(cartID).get(product) > 0) {
                    for (int i = 0; i < -modifyQuantity; i++) {
                        cartUtil.removeProductFromCart(cartID, product, modifyQuantity);
                        ;
                    }
                    int currentQuantity = cartUtil.getCartContents(cartID).get(product);
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
        String cartID = req.getParameter("id");

        cartUtil.clearCart(cartID);

        if (cartUtil.getCartSize(cartID) == 0) {
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