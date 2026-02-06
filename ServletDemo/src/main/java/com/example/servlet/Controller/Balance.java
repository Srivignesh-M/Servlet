package com.example.servlet.Controller;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import com.example.servlet.DAO.UserDAO;

@WebServlet("/account/balance")
public class Balance extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
		HttpSession session =request.getSession(false);
		response.setContentType("application/json");
		if(session!=null&&session.getAttribute("id")!=null) {
			int id=(int)session.getAttribute("id");
			UserDAO userDAO=new UserDAO();
			double balance=userDAO.balanceCheck(id);
			if(balance==-1.0)
			{
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            response.getWriter().write("""
	                {
	                  "error": "user not found"
	                }
	            """);

			}
			response.getWriter().println("{\"status\":\"success\""
					+ ",\"balance\":\""+balance+"\"}");
		}
		else {
			 response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            response.getWriter().write("""
	                {
	                  "error": "Please login"
	                }
	            """);
		}			
	}       
}
