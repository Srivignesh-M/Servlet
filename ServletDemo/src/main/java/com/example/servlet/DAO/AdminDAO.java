package com.example.servlet.DAO;

import com.example.servlet.Models.User;
import com.example.servlet.util.DBConnection;
import com.example.servlet.util.SecurityUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class AdminDAO{
public static List<User> getUsers() {
	
		Connection con;
		List<User> users = new ArrayList<>();
		try {
			con = DBConnection.getConnection();
			String sql="select id,username,email,balance from users";
			PreparedStatement ps=con.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				User user=new User();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setEmail(SecurityUtil.decrypt(rs.getString("email")));
				user.setBalance(rs.getDouble("balance"));
				users.add(user);
			}
			
			ps.close();	
			con.close();
			return users;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
}
	
}
