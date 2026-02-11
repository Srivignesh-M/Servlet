package com.example.servlet.Controller;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.example.servlet.DAO.UserDAO;
import com.example.servlet.Models.User;
import com.example.servlet.util.PasswordUtil;

@WebServlet("/login")
public class Login extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
		String username=request.getParameter("username");
		String pass=request.getParameter("pass");
		
        response.setContentType("application/json");
        UserDAO userDAO=new UserDAO();
        User user=userDAO.login(username,pass);
        HttpSession session =request.getSession(true);
       if(user!=null) {
    	   session.setAttribute("id",user.getId());
    	   session.setAttribute("role", user.getRole());
    	   session.setMaxInactiveInterval(5*60);
    	   response.setStatus(HttpServletResponse.SC_OK);
    	   response.getWriter().println("{\"status\":\"success\""
					+ ",\"message\":\"user Login Successfull\"}");
       }			
       else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println("{\"status\":\"failed\""
					+ ",\"message\":\"no user found\"}");  
       }
    }
}