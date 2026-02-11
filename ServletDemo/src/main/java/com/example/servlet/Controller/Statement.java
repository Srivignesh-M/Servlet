package com.example.servlet.Controller;

import java.io.IOException;
import java.util.List;

import com.example.servlet.DAO.TransactionDAO;
import com.example.servlet.Models.Transaction;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/user/statement")
public class Statement extends HttpServlet {
	private TransactionDAO transactionDAO;
	Statement(){
		this.transactionDAO=new TransactionDAO();
	}
	Statement(TransactionDAO transactionDAO){
		this.transactionDAO=transactionDAO;
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session=request.getSession(false);
		int id=(int)session.getAttribute("id");
		response.setContentType("application/json");
		List<Transaction> transactions= transactionDAO.getTransactions(id);
		Gson gson = new Gson();
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println(gson.toJson(transactions).toString());
	}
}
