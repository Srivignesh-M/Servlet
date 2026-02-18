package servlet.Controller;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlet.DAO.UserDAO;
import servlet.util.RegexUtil;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@WebServlet("/register")
public class Register extends HttpServlet  {
	    private static final Logger logger = LoggerFactory.getLogger(Register.class);
		UserDAO userDAO;
		public Register(){
			this.userDAO=new UserDAO();
		}
		public Register(UserDAO userDAO){
			this.userDAO=userDAO;
		}
		protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	    	
			String username=request.getParameter("username");
			String email = request.getParameter("email");
			String pass=request.getParameter("pass");
			String role=request.getParameter("role");
			if(!RegexUtil.isValidUsername(username)) {
				response.setStatus(400);
				response.getWriter().println("{\"status\":\"failed\"" + ",\"message\":\"username must contains a-z or A-Z or 0-9 or _ (Minimum 5 and Maxium 15 characters is allowded)\"}");
				logger.info("invalid username format");
				return;
			}
			if(!RegexUtil.isValidEmail(email)) {
				response.setStatus(400);
				response.getWriter().println("{\"status\":\"failed\"" + ",\"message\":\"Enter valid email\"}");
				logger.info("invalid email format");
				return;
			}
			if(!RegexUtil.isValidPassword(pass)) {
				response.setStatus(400);
				response.getWriter().println("{\"status\":\"failed\"" + ",\"message\":\"Password must contains a small case alphabet , capital caase alphabet and a number(Minimum 8 and Maximum 20 character only allowded\"}");
				logger.info("invalid password format");
				return;
			}
			
	        response.setContentType("application/json");
	        try {
				userDAO.registerUser(username, email, pass,role);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println("{\"status\":\"success\""
						+ ",\"message\":\"registered Please Login\"}");
				logger.info(username + " Register successfully");
			} catch (Exception e) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().println("{\"status\":\"failed\""
						+ ",\"message\":\"Not registered\"}");
				logger.error(username + " Registration failed");
			}
	    }
	}

