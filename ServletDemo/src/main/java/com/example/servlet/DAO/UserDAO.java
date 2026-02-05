package com.example.servlet.DAO;

import com.example.servlet.Models.User;
import com.example.servlet.util.DBConnection;
import java.sql.*;
public class UserDAO{
public void registerUser(String username, String email, String password) throws Exception {
	
	
	Connection con=DBConnection.getConnection();
	String sql="Insert into users(username,email,password) values(?,?,?);";
	PreparedStatement ps=con.prepareStatement(sql);
	ps.setString(1,username);
	ps.setString(2,email);
	ps.setString(3,password);
	ps.executeUpdate();
	ps.close();
}


public User login(String username,String password)throws Exception {
	Connection con=DBConnection.getConnection();
	String sql="select * from users where username=? AND password=?";
	PreparedStatement ps=con.prepareStatement(sql);
	ps.setString(1,username);
	ps.setString(2,password);
	ResultSet rs=ps.executeQuery();
	User user=new User();
	if(rs.next()) {
	user.setId(rs.getInt("id"));
	user.setUsername(rs.getString("username"));
	user.setBalance(rs.getDouble("balance"));
	return user;
	}
	return null;
}
}
