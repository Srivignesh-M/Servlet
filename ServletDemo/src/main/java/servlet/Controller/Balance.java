package servlet.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servlet.DAO.UserDAO;
import servlet.util.DBConnection;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/user/balance")
public class Balance extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);
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
		logger.info(id+" fetched their balance");
	}
}
