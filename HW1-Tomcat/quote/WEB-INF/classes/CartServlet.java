import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {

    // Helper method to escape special JSON characters in strings
    private String escapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\b", "\\b")
                  .replace("\f", "\\f")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    // Helper method to compute the total cost of items in the cart
    private double computeCost(List<String> cart) {
        double total = 0.0;
        for (String product : cart) {
            if ("JBL Speaker".equals(product)) {
                total += 450;
            } else if ("PTZ3 Camera".equals(product)) {
                total += 13000;
            } else if ("Sennheiser e835 Microphone".equals(product)) {
                total += 900;
            }
        }
        return total;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        List<String> cart = (List<String>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        String action = req.getParameter("action");
        String product = req.getParameter("product");

        if ("add".equals(action) && product != null) {
            cart.add(product);
        } else if ("clear".equals(action)) {
            cart.clear();
        }

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        // Include both the count and the computed cost in the JSON response
        out.write("{\"count\": " + cart.size() + ", \"cost\": " + computeCost(cart) + "}");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        List<String> cart = (List<String>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        // Manually build the JSON response including count, cost, and items array
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"count\": ").append(cart.size()).append(", ");
        sb.append("\"cost\": ").append(computeCost(cart)).append(", ");
        sb.append("\"items\": [");

        for (int i = 0; i < cart.size(); i++) {
            String item = cart.get(i);
            sb.append("\"").append(escapeJson(item)).append("\"");
            if (i < cart.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]}");

        out.write(sb.toString());
    }
}
