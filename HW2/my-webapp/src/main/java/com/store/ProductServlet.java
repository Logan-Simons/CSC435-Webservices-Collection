package com.store;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(urlPatterns = { "/products", "/products/create", "/products/update", "/products/delete" })
public class ProductServlet extends HttpServlet {

    private HashMap<Integer, Product> productCatalog = new HashMap<>();  // Changed to HashMap
    private AtomicInteger productIdGenerator = new AtomicInteger(1);

    public ProductServlet() {
        // Initialize products from ProductData
        ProductData productData = new ProductData();
        productCatalog = productData.getInitialProducts();
        productIdGenerator.set(productCatalog.size() + 1);  // Set ID generator to next available ID
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String servletPath = req.getPathInfo();
        if (servletPath == null || servletPath.equals("/")) {
            handleGetProducts(req, resp);  // Fixed method name
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleGetProducts(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONArray productsArray = new JSONArray();

        for (Product product : productCatalog.values()) {
            JSONObject jsonProduct = new JSONObject();
            jsonProduct.put("id", product.getProductID());
            jsonProduct.put("name", product.getProductName());
            jsonProduct.put("price", product.getProductCost());
            productsArray.put(jsonProduct);
        }

        JSONObject responseJson = new JSONObject();
        responseJson.put("Products", productsArray);
        writeResponse(resp, responseJson);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String servletPath = req.getServletPath();
        if ("/products/create".equals(servletPath)) {
            handleCreateProduct(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", "Invalid method");
            writeResponse(resp, errorJson);
        }
    }

    private void handleCreateProduct(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        String paramPrice = req.getParameter("price");

        JSONObject responseJson = new JSONObject();

        if (name == null || name.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseJson.put("error", "Missing or empty name field");
        } else if (paramPrice == null || paramPrice.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseJson.put("error", "Missing or empty price field");
        } else {
            try {
                double price = Double.parseDouble(paramPrice);
                int newProductID = productIdGenerator.getAndIncrement();
                Product newProduct = new Product(newProductID, name, price);
                productCatalog.put(newProductID, newProduct);

                responseJson.put("message", "Product created successfully!");
                responseJson.put("id", newProductID);
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                responseJson.put("error", "Invalid price format");
            }
        }

        writeResponse(resp, responseJson);
    }

    @Override
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

    private void handleUpdateProduct(HttpServletRequest req, HttpServletResponse resp) {
        JSONObject responseJson = new JSONObject();

        String idParam = req.getParameter("id");
        String priceParam = req.getParameter("price");

        if (idParam == null || idParam.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseJson.put("error", "Missing or empty id field");
            writeResponse(resp, responseJson);
            return;
        }
        if (priceParam == null || priceParam.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseJson.put("error", "Missing or empty price field");
            writeResponse(resp, responseJson);
            return;
        }

        try {
            int updateID = Integer.parseInt(idParam);
            double newPrice = Double.parseDouble(priceParam);

            Product product = productCatalog.get(updateID);
            if (product != null) {
                double oldPrice = product.getProductCost();
                product.setProductCost(newPrice);
                responseJson.put("id", product.getProductID());
                responseJson.put("name", product.getProductName());
                responseJson.put("old_price", oldPrice);
                responseJson.put("updated_price", product.getProductCost());
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                responseJson.put("error", "No product found for ID=" + updateID);
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseJson.put("error", "Invalid id or price format");
        }

        writeResponse(resp, responseJson);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        JSONObject responseJson = new JSONObject();

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseJson.put("error", "Missing or empty id parameter");
            writeResponse(resp, responseJson);
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            Product removedProduct = productCatalog.remove(id);
            if (removedProduct != null) {
                resp.setStatus(HttpServletResponse.SC_OK);
                responseJson.put("deleted", removedProduct.getProductID());
                responseJson.put("name", removedProduct.getProductName());
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                responseJson.put("error", "No product found for ID=" + id);
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseJson.put("error", "Invalid id format");
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