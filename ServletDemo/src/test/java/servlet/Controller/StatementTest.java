package servlet.Controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servlet.Controller.Statement;
import servlet.DAO.TransactionDAO;
import servlet.Models.Transaction;

public class StatementTest {
	private Statement statement;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	private TransactionDAO transactionDAOMock;
	
	@BeforeEach
	void setUp(){
		transactionDAOMock=mock(TransactionDAO.class);
		statement=new Statement(transactionDAOMock);
		request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
	}
	@Test
	void validSatement()throws Exception{
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("id")).thenReturn(5);
		when(request.getParameter("page")).thenReturn("1");
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		transactions.add(new Transaction(5,5,100.0,"debit"));
		when(transactionDAOMock.getTransactions(5,1)).thenReturn(transactions);
		StringWriter stringWriter=new StringWriter();
		PrintWriter printWriter=new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(printWriter);	
		statement.doGet(request,response);
		verify(response).setStatus(HttpServletResponse.SC_OK);
		String result = stringWriter.toString();
		assertTrue(result.contains("\"status\":\"success\""));
	}
	
	@Test
	void testEmptyStatement()throws Exception{
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("id")).thenReturn(5);
		StringWriter stringWriter=new StringWriter();
		PrintWriter printWriter=new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(printWriter);		
		when(transactionDAOMock.getTransactions(anyInt(),anyInt())).thenReturn (new ArrayList<>());
		statement.doGet(request,response);
		verify(response).setStatus(HttpServletResponse.SC_OK);
		String result = stringWriter.toString();
		assertTrue(result.contains("no transactions made"));
	}
}
