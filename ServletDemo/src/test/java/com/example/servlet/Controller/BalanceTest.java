package com.example.servlet.Controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class BalanceTest {
	private Balance balance;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	
	@BeforeEach
	void setUp(){
		balance=new Balance();
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
		verify(response).setStatus(HttpServletResponse.SC_OK);
		String result = stringWriter.toString();
        assertTrue(result.contains("\"status\":\"success\""));
	}
	@Test
	void testNoSession() throws Exception{
		when(request.getSession(false)).thenReturn(null);
		balance.doGet(request, response);
		verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}
	@Test
	void testNoId() throws Exception{
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("id")).thenReturn(null);
		balance.doGet(request, response);
		verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}


}
