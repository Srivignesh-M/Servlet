package com.example.servlet.Controller;

import java.io.IOException;

import com.example.servlet.DAO.TransactionDAO;
import com.example.servlet.DAO.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/user/credit")
public class Credit extends HttpServlet {
	private UserDAO userDAO;
	private TransactionDAO transactionDAO;
	public Credit() {
        this.userDAO = new UserDAO();
        this.transactionDAO=new TransactionDAO();
    }
    public Credit(UserDAO userDAO,TransactionDAO transactionDAO) {
        this.userDAO = userDAO;
        this.transactionDAO=transactionDAO;
    }
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession(false);
		int from_id=(int)(session.getAttribute("id"));
		response.setContentType("application/json");
		int to_id = Integer.parseInt(request.getParameter("id"));
		double amount = Double.parseDouble(request.getParameter("amount"));
		if(amount<1) {
			response.setStatus(400);
			response.getWriter().println("{\"status\":\"failed\"" + ",\"message\":\"cannot credit less than 1 rs\"}");
			return;
		}
		transactionDAO.createTransaction(from_id,to_id,amount,"credit");
		userDAO.credit(to_id, amount);
		userDAO.debit(from_id, amount);
		response.setStatus(200);
		response.getWriter().println("{\"status\":\"success\"" + ",\"amount\":\"" + amount + " credited\"}");
	}
}
