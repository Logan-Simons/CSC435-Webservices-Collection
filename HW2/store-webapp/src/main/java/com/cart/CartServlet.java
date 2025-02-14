package com.cart;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    
    // @GET /cart
    // returns items in user cart
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print("{\"message\": \"this is a product in the cart\"}");
        out.flush();

    }


 
    // @POST /cart/add/id=?/
    // adds items in user cart

    // @DELETE /cart/clear


}
