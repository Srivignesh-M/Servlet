package com.example.servlet.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import com.example.servlet.DAO.UserDAO;

@WebServlet("/user/balance")
public class Balance extends HttpServlet {
	UserDAO userDAO;
	public Balance(){
		this.userDAO=new UserDAO();
	}
	Balance(UserDAO userDAO){
		this.userDAO=userDAO;
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		response.setContentType("application/json");
		int id = (int) session.getAttribute("id");
		double balance = userDAO.balanceCheck(id);
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println("{\"status\":\"success\"" + ",\"balance\":\"" + balance + "\"}");
	}
}
