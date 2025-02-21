package com.cart;

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

import netscape.javascript.JSObject;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    
    Cart Cart = new Cart("cart-1");
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



        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");


        String servletPath = req.getServletPath();
        if ("/cart".equals(servletPath)) {
            handleGetProducts(req, resp);
        } else if ("/cart/cost".equals(servletPath)){
            handleCartCost(req, resp);
        } else {
             resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }

        

        JSONObject responseJson = new JSONObject();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");


        responseJson.put("cart-contents", cartArrayJson);
        PrintWriter out = resp.getWriter();
        out.write(responseJson.toString());
        out.flush();

    }

    // @GET
    // /products
    // returns the list of products, including ID, name, and cost
    private void handleGetCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {

            
         JSONArray cartArrayJson = new JSONArray();
        
        for (int i = 0; i < Cart.getCartSize(); i++) {
            
            JSONObject productObjectJson = new JSONObject();
            productObjectJson.put("cart-index", i);
            productObjectJson.put("name", Cart.getProductByIndex(i).getProductName());
            productObjectJson.put("price", Cart.getProductByIndex(i).getProductCost());
            productObjectJson.put("quantity", Cart.getProductIndexQuantity(i));
            cartArrayJson.put(productObjectJson);
        }


        JSONObject responseJson = new JSONObject();
        responseJson.put("products", productsArray);

        writeResponse(resp, responseJson);

    }



    private void handleCartCost(HttpServlvetRequest req, HttpServletResponse resp) throws IOException {

        JSONObject costJson = new JSONObject();
        costJson.put("cost", Cart.getCartCost(Cart));
        costJson.put("total-items", Cart.getCartSize());

        writeResponse(resp, responseJson);
    }






    protected void doPost(HttpServlvetRequest req, HttpServletResponse resp) {


        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");


        // @POST /cart/add/id=?/
        // adds items in user cart
        String servletPath = req.getServletPath();
        if ("/cart/add/product".equals(servletPath)) {
            handleAddProducts(req, resp);
        } else {
             resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }

    
    }


    private void handleAddProducts(HttpServlvetRequest req, HttpServletResponse resp) {

        int productID = req.getParameter("product-id");
        int modifyQuantity = req.getParameter("quantity");
        Product product = listOfProducts.get(productID);



        if (product != null) {
            
        int currentQuantity = Cart.getProductQuantity(product);


        if (modifyQuantity > 0) {
            if (Cart.getProducts(Cart).contains(product)) {
            Cart.addProduct(product);
            currentQuantity = Cart.getProductQuantity(product);
            responseJson.put("sucess", "product " + product.getProductName() + " now has a quantity " + Cart.getProductQuantity(product) );
            responseJson.put("quantity", currentQuantity);
        
            } else {
                Cart.addProduct(product);
                currentQuantity = Cart.getProductQuantity(product);
                responseJson.put("sucess", "product " + product.getProductName() + " added to cart  " + Cart.getProductQuantity(product) );
                responseJson.put("quantity", currentQuantity);
            }

        if (!Cart.getProducts(Cart).contains(product)) {
            responseJSon.put("error", "product couldn't be added" );
        };

        JSONObject responseJson = new JSONObject();
        
        } else if (Cart.getProducts(Cart).contains(product)) { 
                Cart.removeProduct(product);
                currentQuantity = Cart.getProductQuantity(product);
                responseJson.put("sucess", "removed item from cart");
                responseJSon.put("quantity", Cart.getProductQuantity());
        }
            
         } else {
            responseJSon.put("error", "product not found" );
        }
       

        writeResponse(resp, responseJson);

    }

    // @PUT 
    // /cart/add/id=?/
    protected void doPut(HttpServletRequest req, HttpSerlvetResponse resp) {

    
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String servletPath = req.getServletPath();
        if ("/cart/add/".equals(servletPath)) {
            handleAddProducts(req, resp);
        } else {
             resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }

    }


    
    // @DELETE /cart/clear
    protected void doDelete(HttpServlet req, HttpServletResponse resp) {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String servletPath = req.getServletPath();
        if ("/cart/clear".equals(servletPath)) {

           handleClearCart(req, resp);
            
            }
            
        }

    private void handleClearCart(HttpServlet req, HttpServletResponse resp) {

        JSONObject responseJson = new JSONObject();

         ArrayList<Product> emptyProducts = new ArrayList<>();
            Cart.setProducts(emptyProducts);
            if (Cart.getCartSize() == 0) {
                responseJson.put("message", "success! cart cleared.");
            } else {
                responseJson.put("message", "error clearing cart");
            }


            writeResponse(resp, responseJson);


            }

   

}
