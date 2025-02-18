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

        JSONArray cartArrayJson = new JSONArray();
        JSONObject responseJson = new JSONObject();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        for (int i = 0; i < Cart.getCartSize(); i++) {
            JSONObject productObjectJson = new JSONObject();
            productObjectJson.put("cart-index", i);
            productObjectJson.put("name", Cart.getProductByIndex(i).getProductName());
            productObjectJson.put("price", Cart.getProductByIndex(i).getProductCost());
            productObjectJson.put("quantity", Cart.getProductIndexQuantity(i));
            cartArrayJson.put(productObjectJson);
        }


        responseJson.put("cart-contents", cartArrayJson);
        PrintWriter out = resp.getWriter();
        out.write(responseJson.toString());
        out.flush();

    }


 
    // @POST /cart/add/id=?/
    // adds items in user cart

    // @DELETE /cart/clear


}
