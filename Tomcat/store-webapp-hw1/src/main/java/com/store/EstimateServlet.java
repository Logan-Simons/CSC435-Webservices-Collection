package com.store;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/estimate")
public class EstimateServlet extends HttpServlet {

   @Override
   protected void doPost(HttpServletRequest req, HttpServletResponse resp)
         throws ServletException, IOException {
      // Form Data
      String location = req.getParameter("location");
      String date = req.getParameter("date");
      String time = req.getParameter("time");
      String setup = req.getParameter("setup");
      String teardown = req.getParameter("teardown");
      String isEthernet = req.getParameter("isethernet");
      String numberOfCameras = req.getParameter("cameras");
      String additional = req.getParameter("additional");

      double baseCost = 500.0;
      double numOfCameras = 0;
      double costOfCameras = 0;

      if (isEthernet != null) {
         baseCost = baseCost - 50;
      }

      if (numberOfCameras.equals("1")) {
         numOfCameras = 1;
      } else if (numberOfCameras.equals("2")) {
         numOfCameras = 2;
      } else if (numberOfCameras.equals("3")) {
         numOfCameras = 3;
      }

      costOfCameras = numOfCameras * 1200;
      baseCost = baseCost + costOfCameras;

      // Prepare the response
      resp.setContentType("text/html");
      PrintWriter out = resp.getWriter();
      out.println("<html>");
      out.println("<head><title>Estimation Result</title></head>");
      out.println("<body>");
      out.println("<div style='max-width:600px; margin:auto;'>");
      out.println("<h1>Estimation Result</h1>");
      out.println("<p><strong>Event Location:</strong> " + location + "</p>");
      out.println("<p><strong>Event Date:</strong> " + date + "</p>");
      out.println("<p><strong>Event Time:</strong> " + time + "</p>");
      out.println("<p><strong>Setup Time:</strong> " + setup + "</p>");
      out.println("<p><strong>Teardown Time:</strong> " + teardown + "</p>");
      out.println("<p><strong>Is Ethernet Provided:</strong> " + (isEthernet != null ? "Yes" : "No") + "</p>");
      out.println("<p><strong>Additional Information:</strong> " + additional + "</p>");
      out.println("<h2>Total Estimated Cost: $" + baseCost + "</h2>");
      out.println("</div>");
      out.println("</body>");
      out.println("</html>");
   }
}
