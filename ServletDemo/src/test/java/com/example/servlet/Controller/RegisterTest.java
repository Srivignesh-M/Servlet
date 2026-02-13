package com.example.servlet.Controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.example.servlet.DAO.UserDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RegisterTest {
	private Register register;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private UserDAO userDAO;
	@BeforeEach
	void setUp(){
		userDAO=mock(UserDAO.class);
		register =new Register(userDAO);
		request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
	}
	@Test
	@DisplayName("Register- Should return 200 OK and JSON success when credentials are valid")
	void testValidRegister() throws Exception {
		when(request.getParameter("username")).thenReturn("new");
		when(request.getParameter("email")).thenReturn("new@gmail.com");
		when(request.getParameter("pass")).thenReturn("0123456789");
		when(request.getParameter("role")).thenReturn("USER");
		StringWriter stringWriter=new StringWriter();
		PrintWriter writer=new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		register.doPost(request, response);
		verify(userDAO).registerUser("new", "new@gmail.com", "0123456789","USER");
		verify(response).setStatus(HttpServletResponse.SC_OK);
		String result = stringWriter.toString();
        assertTrue(result.contains("\"status\":\"success\""));
	}
	@Test
	@DisplayName("Register - Should return 404 when any error")
	void testInvalidRegister() throws Exception {
		when(request.getParameter("username")).thenReturn("new");
		when(request.getParameter("email")).thenReturn("new@gmail.com");
		when(request.getParameter("pass")).thenReturn("0123456789");
		when(request.getParameter("role")).thenReturn("USER");
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
