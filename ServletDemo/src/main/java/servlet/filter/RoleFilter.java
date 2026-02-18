package servlet.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/admin/*")
public class RoleFilter implements Filter{
private static final Logger logger = LoggerFactory.getLogger(RoleFilter.class);
public void doFilter(ServletRequest request , ServletResponse response, FilterChain chain) throws IOException, ServletException{
	HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;

    HttpSession session = req.getSession(false);

    String role = (String) session.getAttribute("role");

    if (!"ADMIN".equals(role)) {
        resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        resp.getWriter().write("{\"error\":\"Access denied\"}");
        logger.error("User try to access admin's feautures");
        return;
    }
    chain.doFilter(request, response);
}
}
