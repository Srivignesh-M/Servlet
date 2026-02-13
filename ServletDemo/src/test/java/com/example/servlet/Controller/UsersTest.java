package com.example.servlet.Controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.servlet.DAO.AdminDAO;
import com.example.servlet.Models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UsersTest {
	private Users users;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private AdminDAO adminDAO;
	private StringWriter stringWriter;
	private PrintWriter writer;
	
	@BeforeEach
	void setUp(){
		adminDAO=mock(AdminDAO.class);
		users=new Users(adminDAO);
		request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        stringWriter=new StringWriter();
		writer=new PrintWriter(stringWriter);
	}
	@Test
	void testValidUsers()throws Exception {
		ArrayList<User> list=new ArrayList<User>();
		list.add(new User(1,"vikky","vikky@gmail.com",1000.00));
		when(adminDAO.getUsers()).thenReturn(list);
		when(response.getWriter()).thenReturn(writer);
		users.doGet(request, response);
		verify(response).setStatus(HttpServletResponse.SC_OK);
	}
}
