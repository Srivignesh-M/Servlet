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

@WebServlet("/user/credit")
public class Credit extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(Credit.class);
	private UserDAO userDAO;
	private TransactionDAO transactionDAO;
	private RegexUtil regexUtil;
	public Credit() {
        userDAO = new UserDAO();
        transactionDAO=new TransactionDAO();
        regexUtil=new RegexUtil();
    }
    public Credit(UserDAO userDAO,TransactionDAO transactionDAO,RegexUtil regexUtil) {
        this.userDAO = userDAO;
        this.transactionDAO=transactionDAO;
        this.regexUtil=regexUtil;
    }
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession(false);
		int from_id=(int)(session.getAttribute("id"));
		response.setContentType("application/json");
		int to_id = Integer.parseInt(request.getParameter("id"));
		String amountString=request.getParameter("amount");
		double amount = Double.parseDouble(amountString);
		if(!regexUtil.isValidAmount(amountString)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("{\"status\":\"failed\"" + ",\"message\":\"amount does not contain leading zerosand must contain only two decimals\"}");
			logger.info(from_id+" try to Credit invalid amount format");
			return;
		}
		double balance=userDAO.balanceCheck(from_id);
		if(amount>balance) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("{\"status\":\"failed\"" + ",\"message\":\"invalid amount\"}");
			logger.info(from_id + " try to credit more amount than in their account"+amount);
			return;
		}
		if(from_id!=to_id) {
			userDAO.debit(from_id, amount);
			userDAO.credit(to_id, amount);
			transactionDAO.createTransaction(from_id,to_id, amount, "credit");
		}
		else{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("{\"status\":\"failed\"" + ",\"message\":\"cant send amount to yourself\"}");
			logger.info(from_id+" try to send amount to their own account");
			return;
		}
		response.setStatus(200);
		response.getWriter().println("{\"status\":\"success\"" + ",\"amount\":\"" + amount + " credited\"}");
		logger.info(from_id+" credited "+amount+" Rs. to "+to_id);
	}
}
