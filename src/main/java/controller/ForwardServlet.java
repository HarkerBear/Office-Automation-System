package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ForwardServlet",urlPatterns = "/forward/*")
public class ForwardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri=request.getRequestURI();
        String subUri=uri.substring(1);
        String page=subUri.substring(subUri.indexOf("/"));
        System.out.println(page);
        request.getRequestDispatcher(page+".ftl").forward(request,response);
    }
}
