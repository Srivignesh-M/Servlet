package servlet.Controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servlet.DAO.TransactionDAO;
import servlet.DAO.UserDAO;

@WebServlet("/user/debit")
public class Debit extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(Debit.class);
	private UserDAO userDAO;
	private TransactionDAO transactionDAO;
	public Debit() {
        this.userDAO = new UserDAO();
        this.transactionDAO=new TransactionDAO();
    }
    public Debit(UserDAO userDAO,TransactionDAO transactionDAO) {
        this.userDAO = userDAO;
        this.transactionDAO=transactionDAO;
    }
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession(false);
		response.setContentType("application/json");
		double amount = Double.parseDouble(request.getParameter("amount"));
		int id = (int) session.getAttribute("id");
		if(amount<1) {
			response.setStatus(400);
			response.getWriter().println("{\"status\":\"failed\"" + ",\"message\":\"cannot debit less than 1 rs\"}");
			logger.info(id + " try to debit less than a rupee");
			return;
		}
		
			double balance=userDAO.balanceCheck(id);
			if(amount>balance) {
				response.setStatus(400);
				response.getWriter().println("{\"status\":\"failed\"" + ",\"message\":\"invalid amount\"}");
				logger.info(id + " try to debit more amount than in their account");
				return;
			}
			userDAO.debit(id, amount);
			transactionDAO.createTransaction(id,id,amount,"debit");
			response.setStatus(200);
			response.getWriter().println("{\"status\":\"success\"" + ",\"amount\":\"" + amount + " debited\"}");
			logger.info(id+" debited "+amount +"rs from their account");
			
	}
}
