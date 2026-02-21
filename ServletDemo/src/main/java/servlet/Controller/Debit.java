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
import servlet.util.RegexUtil;

@WebServlet("/user/debit")
public class Debit extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(Debit.class);
	private UserDAO userDAO;
	private TransactionDAO transactionDAO;
	private RegexUtil regexUtil;

	public Debit() {
		userDAO = new UserDAO();
		transactionDAO = new TransactionDAO();
		regexUtil = new RegexUtil();
	}

	public Debit(UserDAO userDAO, TransactionDAO transactionDAO, RegexUtil regexUtil) {
		this.userDAO = userDAO;
		this.transactionDAO = transactionDAO;
		this.regexUtil = regexUtil;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession(false);
		response.setContentType("application/json");
		String amountString = request.getParameter("amount");
		double amount = Double.parseDouble(amountString);
		int id = (int) session.getAttribute("id");
		if (!regexUtil.isValidAmount(amountString)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("{\"status\":\"failed\""
					+ ",\"message\":\"amount does not contain leading zerosand must contain only two decimals\"}");
			logger.info(id + " try to debit invalid amount format");
			return;
		}
		double balance = userDAO.balanceCheck(id);
		if (amount > balance) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("{\"status\":\"failed\"" + ",\"message\":\"invalid amount\"}");
			logger.info(id + " try to debit more amount than in their account");
			return;
		}
		userDAO.debit(id, amount);
		transactionDAO.createTransaction(id, id, amount, "debit");
		response.setStatus(200);
		response.getWriter().println("{\"status\":\"success\"" + ",\"amount\":\"" + amount + " debited\"}");
		logger.info(id + " debited " + amount + "rs from their account");

	}
}
