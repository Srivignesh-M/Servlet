package com.example.servlet.Controller;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.example.servlet.DAO.UserDAO;
@WebServlet("/register")
public class Register extends HttpServlet  {

	private static final long serialVersionUID = 1L;

		protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	    	
			String username=request.getParameter("username");
			String email=request.getParameter("email");
			String pass=request.getParameter("pass");
			
	        response.setContentType("application/json");
	        UserDAO user=new UserDAO();
	        try {
				user.registerUser(username, email, pass);
				response.getWriter().println("{\"status\":\"success\""
						+ ",\"message\":\"user registered Please Login\"}");
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(404);
				response.getWriter().println("{\"status\":\"failed\""
						+ ",\"message\":\"user not registered\"}");
			}
	        
	        
	    }
	}

