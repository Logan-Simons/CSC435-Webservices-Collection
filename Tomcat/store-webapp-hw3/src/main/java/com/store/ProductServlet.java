package com.store;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.print.attribute.standard.JobName;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.store.ProductUtil;
import com.store.Product;

@WebServlet(urlPatterns = { "/products", "/products/create", "/products/update", "/products/delete", "/products/id" })
public class ProductServlet extends HttpServlet {

    private ProductUtil productUtil = new ProductUtil();
    private AtomicInteger productIdGenerator = new AtomicInteger(1); // ID generator for now

    public ProductServlet() {

    }

    @Override
    // @GET request filter
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String servletPath = req.getServletPath();
        if (servletPath == null || servletPath.equals("/products")) {

            handleGetProducts(req, resp);
        } else if (servletPath.equals("/products/id")) {
            handleGetProduct(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleGetProduct(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        JSONObject responseJson = new JSONObject();
        int productID = Integer.parseInt(req.getParameter("product"));
        Product product = productUtil.getProduct(productID);

        if (productID > 0 && (product.getProductID() > 0)) {

            // construct jsonObject
            JSONObject jsonProduct = new JSONObject();
            jsonProduct.put("id", product.getProductID());
            jsonProduct.put("name", product.getProductName());
            jsonProduct.put("description", product.getProductDescription());
            jsonProduct.put("price", product.getProductCost());

            responseJson.put("Product", jsonProduct);
        } else {
            responseJson.put("Error", "invalid ID");
        }

        writeResponse(resp, responseJson);
    }

    // @GET /product request handler
    private void handleGetProducts(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        JSONArray productsArray = new JSONArray();

        HashMap<Integer, Product> productCatalog = productUtil.getProducts();

        for (Product product : productCatalog.values()) {
            JSONObject jsonProduct = new JSONObject();
            jsonProduct.put("id", product.getProductID());
            jsonProduct.put("name", product.getProductName());
            jsonProduct.put("description", product.getProductDescription());
            jsonProduct.put("price", product.getProductCost());
            productsArray.put(jsonProduct);
        }

        JSONObject responseJson = new JSONObject();
        responseJson.put("Products", productsArray);
        writeResponse(resp, responseJson);
    }

    // @POST request filter
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

    // @POST
    // products/add handler
    private void handleCreateProduct(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        String paramPrice = req.getParameter("price");
        String paramDesc = req.getParameter("description");

        JSONObject responseJson = new JSONObject();

        if (name == null || name.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseJson.put("error", "Missing or empty name field");
        } else if (paramPrice == null || paramPrice.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseJson.put("error", "Missing or empty price field");
        } else {
            try {
                int newProductID = (int) (Math.random() * 10000);
                double price = Double.parseDouble(paramPrice);

                Product newProduct = new Product(newProductID, name, paramDesc, price);

                if (productUtil.createProduct(newProduct)) {
                    responseJson.put("message", "Product created successfully!");
                    responseJson.put("id", newProductID);
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                } else {
                    responseJson.put("message", "Error creating product");
                }

            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                responseJson.put("error", "Invalid price format");
            }
        }

        writeResponse(resp, responseJson);
    }

    // @PUT request filter
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

    // @PUT update a product
    protected void handleUpdateProduct(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Example: update product price/name based on id.
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        JSONObject responseJson = new JSONObject();
        String idParam = req.getParameter("id");
        String priceParam = req.getParameter("price");
        String name = req.getParameter("name");
        String description = req.getParameter("description");

        if (idParam == null || idParam.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseJson.put("error", "Missing or empty id field");
            writeResponse(resp, responseJson);
            return;
        }

        try {
            int productID = Integer.parseInt(idParam);
            // Get the current product from the DB
            HashMap<Integer, Product> products = productUtil.getProducts();
            Product product = products.get(productID);

            if (product != null) {
                if (name != null && !name.isEmpty()) {
                    product.setProductName(name);
                }
                if (description != null && !description.isEmpty()) {
                    product.setProductDescription(description);
                }
                if (priceParam != null && !priceParam.isEmpty()) {
                    double newPrice = Double.parseDouble(priceParam);
                    product.setProductCost(newPrice);
                }
                if (productUtil.updateProduct(product)) {
                    responseJson.put("message", "Product updated successfully");
                    responseJson.put("id", product.getProductID());
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    responseJson.put("error", "Failed to update product");
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                responseJson.put("error", "Product not found for ID=" + productID);
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseJson.put("error", "Invalid id or price format");
        }
        writeResponse(resp, responseJson);
    }

    // @DELETE request handle
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
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
            if (productUtil.deleteProduct(id)) {
                responseJson.put("message", "Product deleted successfully");
                responseJson.put("deleted", id);
                resp.setStatus(HttpServletResponse.SC_OK);
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