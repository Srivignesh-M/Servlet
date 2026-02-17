package com.example.servlet.DAO;

import com.example.servlet.Models.User;
import com.example.servlet.util.DBConnection;
import com.example.servlet.util.SecurityUtil;

import java.sql.*;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class AdminDAO{
	private static final Logger logger = LoggerFactory.getLogger(AdminDAO.class);
	public  ArrayList<User> getUsers() {
		ArrayList<User> users = new ArrayList<>();
		String sql="select id,username,email,balance from users";
		try (Connection con= DBConnection.getConnection();
			PreparedStatement ps=con.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();){
			while(rs.next()) {
				User user=new User();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setEmail(SecurityUtil.decrypt(rs.getString("email")));
				user.setBalance(rs.getDouble("balance"));
				users.add(user);
			}
			return users;
		} catch (Exception e) {
			logger.error("Users fetching failed "+e.getMessage()+" "+e);
		}
		return users;
}
	
}
