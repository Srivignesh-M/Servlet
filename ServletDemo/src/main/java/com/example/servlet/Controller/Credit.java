package com.example.servlet.Controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.servlet.DAO.TransactionDAO;
import com.example.servlet.DAO.UserDAO;
import com.example.servlet.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/user/credit")
public class Credit extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(Credit.class);
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
		double balance=userDAO.balanceCheck(from_id);
		if(amount<1) {
			response.setStatus(400);
			response.getWriter().println("{\"status\":\"failed\"" + ",\"message\":\"cannot credit less than 1 rs\"}");
			logger.info(from_id+" try to send less than a rupee");
			return;
		}
		if(amount>balance) {
			response.setStatus(400);
			response.getWriter().println("{\"status\":\"failed\"" + ",\"message\":\"invalid amount\"}");
			logger.info(from_id + " try to credit more amount than in their account");
			return;
		}
		transactionDAO.createTransaction(from_id,to_id,amount,"credit");
		if(from_id==to_id) {
			response.setStatus(400);
			response.getWriter().println("{\"status\":\"failed\"" + ",\"message\":\"cant send amount to yourself\"}");
			logger.info(from_id+" try to send amount to their own account");
			return;
		}
		else{
			userDAO.credit(to_id, amount);
			userDAO.debit(from_id, amount);
		}
		response.setStatus(200);
		response.getWriter().println("{\"status\":\"success\"" + ",\"amount\":\"" + amount + " credited\"}");
		logger.info(from_id+" credited "+amount+" Rs. to "+to_id);
	}
}
