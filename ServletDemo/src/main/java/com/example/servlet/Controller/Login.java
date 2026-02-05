package com.example.servlet.Controller;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.example.servlet.DAO.UserDAO;
import com.example.servlet.Models.User;

@WebServlet("/login")
public class Login extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
		String username=request.getParameter("username");
		String pass=request.getParameter("pass");
		
        response.setContentType("application/json");
        UserDAO userDAO=new UserDAO();
        User user=new User();
        try {
			user=userDAO.login(username,pass);
			if(user.getUsername().equalsIgnoreCase(username))
			response.getWriter().println("{\"status\":\"success\""
					+ "{\"message\":\"user Login Successfull\"}");
			
		} catch (Exception e) {
			response.getWriter().println("{\"status\":\"failed\""
					+ ",\"message\":\"no user found\"}");
		}
        
        
    }
}