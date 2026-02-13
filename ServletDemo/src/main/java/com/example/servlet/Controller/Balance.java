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
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		response.setContentType("application/json");
		int id = (int) session.getAttribute("id");
		UserDAO userDAO = new UserDAO();
		double balance = userDAO.balanceCheck(id);
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println("{\"status\":\"success\"" + ",\"balance\":\"" + balance + "\"}");
	}
}
