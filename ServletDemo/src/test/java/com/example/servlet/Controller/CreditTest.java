package com.example.servlet.Controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.servlet.DAO.TransactionDAO;
import com.example.servlet.DAO.UserDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CreditTest {
	private UserDAO userDAOMock;
    private TransactionDAO transactionDAOMock;
	private Credit credit;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	
	@BeforeEach
	void setUp(){
		userDAOMock = mock(UserDAO.class);
        transactionDAOMock = mock(TransactionDAO.class);
		credit=new Credit(userDAOMock,transactionDAOMock);
		request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
	}
	@Test
	void testSuccessCredit() throws Exception{
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("id")).thenReturn(5);
		when(request.getParameter("id")).thenReturn("6");
		when(request.getParameter("amount")).thenReturn("10000.00");
		StringWriter stringWriter=new StringWriter();
		PrintWriter writer=new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		credit.doPost(request, response);
		verify(userDAOMock).credit(6, 10000.00);
		verify(userDAOMock).debit(5, 10000.00);
        verify(transactionDAOMock).createTransaction(5, 6, 10000.00, "credit");
		verify(response).setStatus(200);
		String result = stringWriter.toString();
        assertTrue(result.contains("\"status\":\"success\""));
	}
	@Test
	void testFailedCredit() throws Exception{
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("id")).thenReturn(5);
		when(request.getParameter("id")).thenReturn("6");
		when(request.getParameter("amount")).thenReturn("-10000.00");
		StringWriter stringWriter=new StringWriter();
		PrintWriter writer=new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		credit.doPost(request, response);
		verify(response).setStatus(400);
		String result = stringWriter.toString();
		assertTrue(result.contains("\"status\":\"failed\""));
	}	
}
