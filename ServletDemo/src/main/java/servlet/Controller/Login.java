package servlet.Controller;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servlet.DAO.AdminDAO;
import servlet.DAO.UserDAO;
import servlet.Models.User;
import servlet.util.PasswordUtil;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(Login.class);
	private UserDAO userDAO;
	public Login() {
		userDAO=new UserDAO();
	}
	public Login(UserDAO userDAO) {
		this.userDAO=userDAO;
	}
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
		String username=request.getParameter("username");
		String pass=request.getParameter("pass");
		
        response.setContentType("application/json");
        
        User user=userDAO.login(username,pass);
        HttpSession session =request.getSession(true);
       if(user!=null) {
    	   session.setAttribute("id",user.getId());
    	   session.setAttribute("role", user.getRole());
    	   session.setMaxInactiveInterval(5*60);
    	   response.setStatus(HttpServletResponse.SC_OK);
    	   response.getWriter().println("{\"status\":\"success\""
					+ ",\"message\":\"user Login Successfull\"}");
    	   logger.info(username + " logged in");
       }			
       else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println("{\"status\":\"failed\""
					+ ",\"message\":\"no user found\"}");  
			logger.error(username + " logged in failed");
       }
    }
}