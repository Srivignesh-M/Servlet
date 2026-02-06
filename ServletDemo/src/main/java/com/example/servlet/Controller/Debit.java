package com.example.servlet.Controller;

import java.io.IOException;

import com.example.servlet.DAO.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/account/debit")
public class Debit extends  HttpServlet{
public void doPost (HttpServletRequest request,HttpServletResponse response)throws IOException,ServletException{
	HttpSession session =request.getSession(false);
	response.setContentType("application/json");
	int id=Integer.parseInt(request.getParameter("id"));
	double amount=Double.parseDouble(request.getParameter("amount"));
	
	if(session!=null&&session.getAttribute("id")!=null) {
		UserDAO userDAO=new UserDAO();
		userDAO.debit(id,amount);
		
		response.getWriter().println("{\"status\":\"success\""
				+",\"amount\":\""+amount+"\"}");
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
