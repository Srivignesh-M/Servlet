package com.example.servlet.DAO;

import com.example.servlet.Models.User;
import com.example.servlet.util.DBConnection;
import com.example.servlet.util.PasswordUtil;

import java.sql.*;
public class UserDAO{
public void registerUser(String username, String email, String password) throws Exception {
	
	
	Connection con=DBConnection.getConnection();
	String sql="Insert into users(username,email,password) values(?,?,?);";
	PreparedStatement ps=con.prepareStatement(sql);
	ps.setString(1,username);
	ps.setString(2,email);
	ps.setString(3,PasswordUtil.hashPassword(password));
	ps.executeUpdate();
	ps.close();
	con.close();
}


public User login(String username,String pass){
	
	Connection con;
	try {
		con = DBConnection.getConnection();
		String sql="select * from users where username=? ";
		PreparedStatement ps=con.prepareStatement(sql);
		ps.setString(1,username);
		ResultSet rs=ps.executeQuery();
		User user=new User();
		if(rs.next()) {
		user.setId(rs.getInt("id"));
//		user.setUsername(rs.getString("username"));
//		user.setPassword(rs.getString("password"));
//		user.setBalance(rs.getDouble("balance"));
		}
		if(PasswordUtil.checkPassword(pass, rs.getString("password")))
		{
			return user;
		}
		ps.close();	
		con.close();
	} catch (Exception e) {
		e.printStackTrace();
	}	
	return null;
}
public double  balanceCheck(int id){
	Connection con;
	try {
		con = DBConnection.getConnection();
		String sql="select balance from users where id=? ";
		PreparedStatement ps=con.prepareStatement(sql);
		ps.setInt(1,id);
		ResultSet rs=ps.executeQuery();
		rs.next();
		double balance=rs.getDouble("balance");
		ps.close();	
		con.close();
		return balance;
	} catch (Exception e) {
		e.printStackTrace();
	}
	return -1.0;
	
}


public void credit(int id, double amount) {
	Connection con;
	try {
		con = DBConnection.getConnection();
		String sql="UPDATE users SET balance = balance + ? WHERE id = ?;";
		PreparedStatement ps=con.prepareStatement(sql);
		ps.setDouble(1, amount);
		ps.setDouble(2,id);
		ps.executeUpdate();
		ps.close();	
		con.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
	
}


public void debit(int id, double amount) {
	Connection con;
	try {
		con = DBConnection.getConnection();
		String sql="UPDATE users SET balance = balance - ? WHERE id = ?;";
		PreparedStatement ps=con.prepareStatement(sql);
		ps.setDouble(1, amount);
		ps.setDouble(2,id);
		ps.executeUpdate();
		ps.close();	
		con.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
	
}
}
