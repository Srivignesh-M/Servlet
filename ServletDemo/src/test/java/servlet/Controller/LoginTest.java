package servlet.Controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servlet.Controller.Login;
import servlet.DAO.UserDAO;
import servlet.Models.User;

public class LoginTest {
	private Login login;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	private UserDAO userDAO;
	private User user;
	@BeforeEach
	void setUp(){
		userDAO=mock(UserDAO.class);
		login =new Login(userDAO);
		request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session=mock(HttpSession.class);
	}
	@Test
	@DisplayName("Login - Should return 200 OK and JSON success when credentials are valid")
	void testValidLogin() throws Exception {
		when(request.getSession(true)).thenReturn(session);
		when(request.getParameter("username")).thenReturn("vikky");
		when(request.getParameter("pass")).thenReturn("12345678");
		StringWriter stringWriter=new StringWriter();
		PrintWriter writer=new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		user=new User(5,"USER");
		when(userDAO.login("vikky","12345678")).thenReturn(user);
		login.doPost(request, response);
		verify(userDAO).login("vikky", "12345678");
		verify(response).setStatus(HttpServletResponse.SC_OK);
		String result = stringWriter.toString();
        assertTrue(result.contains("\"status\":\"success\""));
	}
	@Test
	@DisplayName("Login - Should return 404 Not Found when user does not exist in DB")
	void testInvalidLogin() throws Exception {
		when(request.getSession(true)).thenReturn(session);
		when(request.getParameter("username")).thenReturn("abcd");
		when(request.getParameter("pass")).thenReturn("12345678");
		StringWriter stringWriter=new StringWriter();
		PrintWriter writer=new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		login.doPost(request, response);
		verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
		String result = stringWriter.toString();
        assertTrue(result.contains("\"status\":\"failed\""));
	}
}
