package servlet.Controller;

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
import servlet.Controller.Debit;
import servlet.DAO.TransactionDAO;
import servlet.DAO.UserDAO;

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
		when(userDAOMock.balanceCheck(5)).thenReturn(20000.00);
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
	void testFailedForLowAmount() throws Exception{
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
	@Test
	void testFailedForLowBalance() throws Exception{
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("id")).thenReturn(5);
		when(request.getParameter("amount")).thenReturn("10000.00");
		when(userDAOMock.balanceCheck(5)).thenReturn(2000.00);
		StringWriter stringWriter=new StringWriter();
		PrintWriter writer=new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		debit.doPost(request, response);
		verify(response).setStatus(400);
		String result = stringWriter.toString();
		assertTrue(result.contains("\"status\":\"failed\""));
	}
}
