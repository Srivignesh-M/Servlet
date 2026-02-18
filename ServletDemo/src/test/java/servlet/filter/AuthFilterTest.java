package servlet.filter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servlet.filter.AuthFilter;

public class AuthFilterTest {
	private AuthFilter authFilter;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	private FilterChain filterChain;
	
	@BeforeEach
	void setUp(){
		filterChain=mock(FilterChain.class);
		authFilter=new AuthFilter();
		request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
	}
	@Test
	void testValidSession() throws Exception{
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("id")).thenReturn(5);
		authFilter.doFilter(request, response,filterChain);
		verify(filterChain, times(1)).doFilter(request, response);
	}
	@Test
	void testNoSession() throws Exception{
		when(request.getSession(false)).thenReturn(null);
		StringWriter stringWriter=new StringWriter();
		PrintWriter printWriter=new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(printWriter);
		authFilter.doFilter(request, response,filterChain);
		verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		String result = stringWriter.toString();
		assertTrue(result.contains("{\"error\":\"Login required\"}"));
	}
	@Test
	void testNoId() throws Exception{
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("id")).thenReturn(null);
		StringWriter stringWriter=new StringWriter();
		PrintWriter printWriter=new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(printWriter);
		authFilter.doFilter(request, response,filterChain);
		verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		String result = stringWriter.toString();
		assertTrue(result.contains("{\"error\":\"Login required\"}"));
	}
}
