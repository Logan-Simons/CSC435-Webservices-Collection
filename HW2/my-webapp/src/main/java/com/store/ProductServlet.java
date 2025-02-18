package com.store;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.store.Product;

@WebServlet(urlPatterns = { "/products", "/products/create", "/products/update", "/products/delete" })
public class ProductServlet extends HttpServlet {

    ArrayList<Product> listOfProducts = new ArrayList<>();
    private AtomicInteger productIdGenerator = new AtomicInteger(1); // Ensures unique IDs

    public ProductServlet() {
        createProducts();
        productIdGenerator.set(listOfProducts.size() + 1);
    }

    private void createProducts() {

        Product product1 = new Product(1, "Orange", 3.99);
        Product product2 = new Product(2, "Apple", 4.99);
        Product product3 = new Product(3, "Pear", 5.99);
        listOfProducts.add(product1);
        listOfProducts.add(product2);
        listOfProducts.add(product3);

    }

    // Handle @GET requests on servlet
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String servletPath = req.getServletPath();
        if ("/products".equals(servletPath)) {
            handleGetProducts(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    // @GET
    // /products
    // returns the list of products, including ID, name, and cost
    private void handleGetProducts(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        JSONArray productsArray = new JSONArray();

        for (int i = 0; i < listOfProducts.size(); i++) {

            Product product = listOfProducts.get(i);
            JSONObject jsonProduct = new JSONObject();

            int id = product.getProductID();
            jsonProduct.put("id", id);

            String name = product.getProductName();
            jsonProduct.put("name", name);

            double cost = product.getProductCost();
            jsonProduct.put("cost", cost);

            productsArray.put(jsonProduct);
        }

        JSONObject responseJson = new JSONObject();
        responseJson.put("Products", productsArray);

        writeResponse(resp, responseJson);

    }

    // @POST
    // URL Handler
    // creates a product stored locally in the servlet, returns the ID of the
    // product
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String servletPath = req.getServletPath();
        if ("/products/create".equals(servletPath)) {
            System.out.println(servletPath);
            handleCreateProduct(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", "invalid method");
            writeResponse(resp, errorJson);
        }
    }

    // @POST
    // /products/create?name=?&price=?
    private void handleCreateProduct(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        String paramPrice = req.getParameter("price");

        JSONObject responseJson = new JSONObject();

        if (name == null && paramPrice == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseJson.put("error", "Missing name and price field.");
        } else if (name == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseJson.put("error", "Missing name field.");
        } else if (paramPrice.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseJson.put("error", "Missing price field");
        } else {
            try {
                double price = Double.parseDouble(paramPrice);
                int newProductID = productIdGenerator.getAndIncrement();
                Product newProduct = new Product(newProductID, name, price);

                listOfProducts.add(newProduct);

                responseJson.put("message", "product created successfully!");
                responseJson.put("id", newProductID);
                resp.setStatus(HttpServletResponse.SC_CREATED);

            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                responseJson.put("error", "invalid price format");
            }
        }

        writeResponse(resp, responseJson);
    }

    // @PUT
    // URL Handler for PUT requests
    // finds and updates the price of the product based on the id and updated cost
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String servletPath = req.getServletPath();
        if ("/products/update".equals(servletPath)) {
            handleUpdateProduct(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    // @PUT
    // /products/update?id=?&price=?
    private void handleUpdateProduct(HttpServletRequest req, HttpServletResponse resp) {

        JSONObject responseJson = new JSONObject();

        Integer updateID = Integer.parseInt(req.getParameter("id"));
        Double oldCost;
        Double newCost = Double.parseDouble(req.getParameter("cost"));

        Product desiredProduct;

        if (updateID.equals(null) && newCost.equals(null)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseJson.put("error", "Missing id and price field.");
        } else if (updateID.equals(null)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseJson.put("error", "Missing ID field.");
        } else if (newCost.equals(null)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseJson.put("error", "Missing new cost field");
        } else {

            try {
                for (int i = 0; i < listOfProducts.size(); i++) {
                    if (listOfProducts.get(i).getProductID() == updateID) {
                        oldCost = listOfProducts.get(i).getProductCost();
                        listOfProducts.get(i).setProductCost(newCost);
                        desiredProduct = listOfProducts.get(i);
                        responseJson.put("id", desiredProduct.getProductID());
                        responseJson.put("name", desiredProduct.getProductName());
                        responseJson.put("old_cost", oldCost);
                        responseJson.put("updated_cost", desiredProduct.getProductCost());
                        break;
                    } else if (i == listOfProducts.size()) {
                        responseJson.put("error", "invalid product ID. no product found");
                    }
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                responseJson.put("error", "invalid price format");
            }

            writeResponse(resp, responseJson);

        }
    }

    // @DELETE
    // /products/delete?id=?
    // finds the product based on the ID, and deletes it
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        JSONObject responseJson = new JSONObject();

        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseJson.put("error", "Missing 'id' parameter");
            writeResponse(resp, responseJson);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseJson.put("error", "Invalid 'id' format");
            writeResponse(resp, responseJson);
            return;
        }

        Product desiredProduct = null;
        boolean foundProduct = false;

        for (int i = 0; i < listOfProducts.size(); i++) {
            if (listOfProducts.get(i).getProductID() == id) {
                desiredProduct = listOfProducts.get(i);
                listOfProducts.remove(i);
                foundProduct = true;
                break;
            }
        }

        if (foundProduct) {
            resp.setStatus(HttpServletResponse.SC_OK);
            responseJson.put("deleted", desiredProduct.getProductID());
            responseJson.put("name", desiredProduct.getProductName());
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            responseJson.put("error", "No product found for ID=" + id);
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
