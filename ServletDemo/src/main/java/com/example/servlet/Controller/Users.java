package com.example.servlet.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import com.example.servlet.DAO.AdminDAO;
import com.example.servlet.DAO.UserDAO;
import com.example.servlet.Models.User;
import com.google.gson.Gson;

@WebServlet("/admin/users")
public class Users extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");
		List<User> users= AdminDAO.getUsers();
		Gson gson = new Gson();
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println(gson.toJson(users).toString());
		
		
	}
}
