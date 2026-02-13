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
		UserDAO userDAO;
		public Register(){
			this.userDAO=new UserDAO();
		}
		public Register(UserDAO userDAO){
			this.userDAO=userDAO;
		}
		protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	    	
			String username=request.getParameter("username");
			String email = request.getParameter("email");
			String pass=request.getParameter("pass");
			String role=request.getParameter("role");
			
	        response.setContentType("application/json");
	        try {
				userDAO.registerUser(username, email, pass,role);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println("{\"status\":\"success\""
						+ ",\"message\":\"registered Please Login\"}");
			} catch (Exception e) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().println("{\"status\":\"failed\""
						+ ",\"message\":\"Not registered\"}");
			}
	    }
	}

