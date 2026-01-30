package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		String username=request.getParameter("username");
		String pass=request.getParameter("pass");
        response.setContentType("text/html");
        response.getWriter().println("<h1>"+username+"</h1><br><h1>"+pass+"</h1>");
    }
}