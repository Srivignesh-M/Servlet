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

public class DebitTest {
	private UserDAO userDAOMock;
    private TransactionDAO transactionDAOMock;
	private Debit debit;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	
	@BeforeEach
	void setUp(){
		userDAOMock = mock(UserDAO.class);
        transactionDAOMock = mock(TransactionDAO.class);
		debit=new Debit(userDAOMock,transactionDAOMock);
		request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
	}
	@Test
	void testSuccessDebit() throws Exception{
		when(request.getSession(false)).thenReturn(session);
		when(request.getParameter("amount")).thenReturn("10000.00");
		when(session.getAttribute("id")).thenReturn(5);
		StringWriter stringWriter=new StringWriter();
		PrintWriter writer=new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		debit.doPost(request, response);
		verify(userDAOMock).debit(5, 10000.00);
        verify(transactionDAOMock).createTransaction(5, 5, 10000.00, "debit");
		verify(response).setStatus(200);
		String result = stringWriter.toString();
        assertTrue(result.contains("\"status\":\"success\""));
	}
	@Test
	void testFailedDebit() throws Exception{
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("id")).thenReturn(5);
		when(request.getParameter("amount")).thenReturn("-10000.00");
		StringWriter stringWriter=new StringWriter();
		PrintWriter writer=new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		debit.doPost(request, response);
		verify(response).setStatus(400);
		String result = stringWriter.toString();
		assertTrue(result.contains("\"status\":\"failed\""));
	}	
}
