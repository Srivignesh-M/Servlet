package com.example.servlet.filter;

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

public class RoleFilterTest {
	private RoleFilter roleFilter;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	private FilterChain filterChain;
	
	@BeforeEach
	void setUp(){
		filterChain=mock(FilterChain.class);
		roleFilter=new RoleFilter();
		request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
	}
	@Test
	void testValidRole() throws Exception{
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("role")).thenReturn("ADMIN");
		roleFilter.doFilter(request, response,filterChain);
		verify(filterChain, times(1)).doFilter(request, response);
	}
	@Test
	void testInvalidRole() throws Exception{
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("role")).thenReturn("USER");
		StringWriter stringWriter=new StringWriter();
		PrintWriter printWriter=new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(printWriter);
		roleFilter.doFilter(request, response,filterChain);
		verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
		String result = stringWriter.toString();
		assertTrue(result.contains("{\"error\":\"Access denied\"}"));
	}
}
