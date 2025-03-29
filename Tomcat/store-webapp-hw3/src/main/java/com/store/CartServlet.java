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
        Integer cartID = Integer.parseInt(req.getParameter("id").toString());
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
        Integer cartID = Integer.parseInt(req.getParameter("id"));
        JSONObject costJson = new JSONObject();
        costJson.put("cost", cartUtil.getCartCost(cartID));
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
            // Retrieve cartID and productID from request
            int cartID = Integer.parseInt(req.getParameter("id"));
            int productID = Integer.parseInt(req.getParameter("product-id"));

            // Get quantity from request; default to 1 if not provided.
            int modifyQuantity = 1;
            String quantityParam = req.getParameter("quantity");
            if (quantityParam != null && !quantityParam.isEmpty()) {
                modifyQuantity = Integer.parseInt(quantityParam);
            }

            // Validate quantity is not zero.
            if (modifyQuantity == 0) {
                responseJson.put("error", "Quantity cannot be zero.");
                writeResponse(resp, responseJson);
                return;
            }

            // Retrieve the product from the database
            Product product = productUtil.getProduct(productID);
            if (product != null) {
                // If quantity is positive, add product; if negative, remove product.
                if (modifyQuantity > 0) {
                    cartUtil.addProductToCart(cartID, product, modifyQuantity);
                } else { // modifyQuantity < 0
                    responseJson.put("Error", "quantity must be greater than 0");
                    return;
                }

                int currentQuantity = cartUtil.getProductQuantityFromCart(cartID, productID);
                responseJson.put("success", "Product " + product.getProductName() +
                        " now has quantity " + currentQuantity);
                responseJson.put("quantity", currentQuantity);
            } else {
                responseJson.put("error", "Invalid product ID");
            }
        } catch (NumberFormatException e) {
            responseJson.put("error", "Invalid quantity or product ID format");
        } catch (Exception e) {
            e.printStackTrace();
            responseJson.put("error", "An unexpected error occurred.");
        }

        writeResponse(resp, responseJson);
    }

    // @PUT handler
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String servletString = req.getPathInfo();
        if (servletString != null && servletString.startsWith("/sub")) {
            handleSubtractProduct(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }

    }

    private void handleSubtractProduct(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        JSONObject responseJson = new JSONObject();

        int cartid = Integer.parseInt(req.getParameter("id"));
        int productid = Integer.parseInt(req.getParameter("product"));

        // Retrieve the product by ID
        Product product = productUtil.getProduct(productid);
        if (product != null) {
            // Adjust the quantity removal as needed; here, we remove 1 unit
            cartUtil.subtractProductFromCart(cartid, product);
            int currentQuantity = cartUtil.getProductQuantityFromCart(cartid, productid);
            responseJson.put("success", "Product " + product.getProductName() +
                    " now has quantity " + currentQuantity);
            responseJson.put("quantity", currentQuantity);
        } else {
            responseJson.put("error", "Invalid product ID");
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
        Integer cartID = Integer.parseInt(req.getParameter("id"));

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