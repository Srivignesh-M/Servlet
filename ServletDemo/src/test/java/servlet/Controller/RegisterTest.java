package servlet.Controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
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
import servlet.Controller.Register;
import servlet.DAO.UserDAO;
import servlet.util.EmailSender;
import servlet.util.RegexUtil;

public class RegisterTest {
	private Register register;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private UserDAO userDAO;
	private RegexUtil regexUtil;
	private EmailSender emailSender;
	@BeforeEach
	void setUp(){
		userDAO=mock(UserDAO.class);
		emailSender=mock(EmailSender.class);
		regexUtil=mock(RegexUtil.class);
		register =new Register(userDAO,regexUtil,emailSender);
		request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
	}
	@Test
	@DisplayName("Register- Should return 200 OK and JSON success when credentials are valid")
	void testValidRegister() throws Exception {
		when(request.getParameter("username")).thenReturn("new");
		when(request.getParameter("email")).thenReturn("new@gmail.com");
		when(request.getParameter("pass")).thenReturn("NewUser@123");
		when(request.getParameter("role")).thenReturn("USER");
		when(regexUtil.isValidUsername("new")).thenReturn(true);
		when(regexUtil.isValidEmail("new@gmail.com")).thenReturn(true);
		when(regexUtil.isValidPassword("NewUser@123")).thenReturn(true);
		StringWriter stringWriter=new StringWriter();
		PrintWriter writer=new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		register.doPost(request, response);
		verify(userDAO).registerUser("new", "new@gmail.com", "NewUser@123","USER");
		verify(response).setStatus(HttpServletResponse.SC_OK);
		Thread.sleep(1000);
		verify(emailSender).send("new@gmail.com","Welcome to Namma Bank","Succesfully Register to Namma Bank . Now you can avail all the services of Namma Bank");
		String result = stringWriter.toString();
        assertTrue(result.contains("\"status\":\"success\""));
	}
	@Test
	@DisplayName("Register - Should return 404 when username is not valid")
	void testInvalidUsername() throws Exception {
		when(request.getParameter("username")).thenReturn("new");
		when(request.getParameter("email")).thenReturn("new@gmail.com");
		when(request.getParameter("pass")).thenReturn("0123456789");
		when(request.getParameter("role")).thenReturn("USER");
		when(regexUtil.isValidUsername("new")).thenReturn(false);
		StringWriter stringWriter=new StringWriter();
		PrintWriter writer=new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		register.doPost(request, response);
		verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
		String result = stringWriter.toString();
        assertTrue(result.contains("\"status\":\"failed\""));
	}@Test
	@DisplayName("Register - Should return 404 when email is not valid")
	void testInvalidEmail() throws Exception {
		when(request.getParameter("username")).thenReturn("new");
		when(request.getParameter("email")).thenReturn("new@gmail.com");
		when(request.getParameter("pass")).thenReturn("0123456789");
		when(request.getParameter("role")).thenReturn("USER");
		when(regexUtil.isValidEmail("new@gmail.com")).thenReturn(false);
		StringWriter stringWriter=new StringWriter();
		PrintWriter writer=new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		register.doPost(request, response);
		verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
		String result = stringWriter.toString();
        assertTrue(result.contains("\"status\":\"failed\""));
	}@Test
	@DisplayName("Register - Should return 404 when password is not valid")
	void testInvalidPassword() throws Exception {
		when(request.getParameter("username")).thenReturn("new");
		when(request.getParameter("email")).thenReturn("new@gmail.com");
		when(request.getParameter("pass")).thenReturn("0123456789");
		when(request.getParameter("role")).thenReturn("USER");
		when(regexUtil.isValidPassword("0123456789")).thenReturn(false);
		StringWriter stringWriter=new StringWriter();
		PrintWriter writer=new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		register.doPost(request, response);
		verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
		String result = stringWriter.toString();
        assertTrue(result.contains("\"status\":\"failed\""));
	}
	@Test
	@DisplayName("Register - Should return 404 when any error")
	void testInvalidRegister() throws Exception {
		when(request.getParameter("username")).thenReturn("new");
		when(request.getParameter("email")).thenReturn("new@gmail.com");
		when(request.getParameter("pass")).thenReturn("0123456789");
		when(request.getParameter("role")).thenReturn("USER");
		when(regexUtil.isValidUsername("new")).thenReturn(true);
		when(regexUtil.isValidEmail("new@gmail.com")).thenReturn(true);
		when(regexUtil.isValidPassword("0123456789")).thenReturn(true);
		doThrow(new Exception("DB Error")).when(userDAO)
        .registerUser("new", "new@gmail.com", "0123456789","USER");
		StringWriter stringWriter=new StringWriter();
		PrintWriter writer=new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		register.doPost(request, response);
		verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
		String result = stringWriter.toString();
        assertTrue(result.contains("\"status\":\"failed\""));
	}
}
