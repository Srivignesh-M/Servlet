package com.example.servlet.Controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.servlet.DAO.UserDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class BalanceTest {
	private Balance balance;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	private UserDAO userDAO;
	
	@BeforeEach
	void setUp(){
		userDAO=mock(UserDAO.class);
		balance=new Balance(userDAO);
		request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
	}
	@Test
	void testSuccessBalance() throws Exception{
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("id")).thenReturn(5);
		StringWriter stringWriter=new StringWriter();
		PrintWriter writer=new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		balance.doGet(request, response);
		verify(userDAO).balanceCheck(5);
		verify(response).setStatus(HttpServletResponse.SC_OK);
		String result = stringWriter.toString();
        assertTrue(result.contains("\"status\":\"success\""));
	}
}
