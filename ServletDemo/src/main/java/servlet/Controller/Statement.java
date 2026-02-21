package servlet.Controller;
import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servlet.DAO.TransactionDAO;
import servlet.Models.Transaction;

@WebServlet("/user/statement")

public class Statement extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(Statement.class);
	private TransactionDAO transactionDAO;
	public Statement(){
		this.transactionDAO=new TransactionDAO();
	}
	public Statement(TransactionDAO transactionDAO){
		this.transactionDAO=transactionDAO;
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session=request.getSession(false);
		int id=(int)session.getAttribute("id");
		ArrayList<Transaction> transactions= transactionDAO.getTransactions(id);
		if(transactions==null || transactions.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println("""
					{
					"status":"success",
					"message":"no transactions made"
					}
					""");
			logger.info(id + "viewed their Statement but no transactions made by them.");
			return;
		}
		Gson gson = new Gson();
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println("""
				{
				"status":"success",
				"message":"these are the transactions"
				}
				""");
		logger.info(id + "viewed their Statement.");
		response.getWriter().println(gson.toJson(transactions).toString());
	}
}
