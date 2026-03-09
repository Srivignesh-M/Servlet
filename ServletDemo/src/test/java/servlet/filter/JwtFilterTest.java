package servlet.filter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servlet.util.JwtUtil;

 class JwtFilterTest {
    private JwtFilter jwtFilter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        filterChain = mock(FilterChain.class);
        jwtFilter = new JwtFilter();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
    }

    @Test
    void testValidJwt() throws IOException, ServletException {
        when(request.getSession(false)).thenReturn(session);
        when(request.getHeader("Authorization")).thenReturn("Bearer asdjs12ejsfjoewqi");
        try (MockedStatic<JwtUtil> mockedJwt = mockStatic(JwtUtil.class)) {
            mockedJwt.when(() -> JwtUtil.isTokenValid("asdjs12ejsfjoewqi")).thenReturn(true);
            jwtFilter.doFilter(request, response, filterChain);
            verify(filterChain, times(1)).doFilter(request, response);
        }
    }
    @Test
    void testInValidJwt() throws IOException, ServletException {
        when(request.getSession(false)).thenReturn(session);
        when(request.getHeader("Authorization")).thenReturn("Bearer asdjs12ejsfjoewqi");        
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
        try (MockedStatic<JwtUtil> mockedJwt = mockStatic(JwtUtil.class)) {
            mockedJwt.when(() -> JwtUtil.isTokenValid("asdjs12ejsfjoewqi")).thenReturn(false);
            jwtFilter.doFilter(request, response, filterChain);
            verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);           
            String result = stringWriter.toString();
            assertTrue(result.contains("{\"status\":\"failed\""));
        }
    }
}