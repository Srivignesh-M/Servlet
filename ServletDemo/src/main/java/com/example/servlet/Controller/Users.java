package com.example.servlet.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import com.example.servlet.DAO.AdminDAO;
import com.example.servlet.Models.User;
import com.google.gson.Gson;

@WebServlet("/admin/users")
public class Users extends HttpServlet {
	AdminDAO adminDAO;
	public Users() {
		adminDAO=new AdminDAO();
	}
	public Users(AdminDAO adminDAO) {
		this.adminDAO=adminDAO;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");
		ArrayList<User> users= adminDAO.getUsers();
		Gson gson = new Gson();
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println(gson.toJson(users).toString());	
	}
}
