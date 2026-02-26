package servlet.DAO;

import servlet.Models.User;
import servlet.util.DBConnection;
import servlet.util.PasswordUtil;
import servlet.util.SecurityUtil;

import java.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDAO{
	private static final Logger logger =LoggerFactory.getLogger(UserDAO.class);
	private Connection con;
	public UserDAO() {
        try{
        	this.con = DBConnection.getConnection();
        }
        catch(SQLException e) {
        	
        	logger.error("database connection error", e);
        }
    }
    public UserDAO(Connection con) {
        this.con = con;
    }
	
public void registerUser(String username, String email, String password,String role) throws Exception {
	String sql="Insert into users(username,email,password,role) values(?,?,?,?);";
	try(PreparedStatement ps=con.prepareStatement(sql);){
	ps.setString(1,username);
	email=SecurityUtil.encrypt(email);
	ps.setString(2,email);
	ps.setString(3,PasswordUtil.hashPassword(password));
	ps.setString(4, role);
	ps.executeUpdate();
	}
	catch(Exception e) {
		e.printStackTrace();
		throw e;
	}
}

public User login(String username,String pass){
	String sql="select id,role,password from users where username=? ";
	try (PreparedStatement ps=con.prepareStatement(sql)){
		ps.setString(1,username);
		ResultSet rs=ps.executeQuery();
		User user=new User();
		if(rs.next()) {
		user.setId(rs.getInt("id"));
		user.setRole(rs.getString("role"));
		
		if(PasswordUtil.checkPassword(pass, rs.getString("password")))
		{
			return user;
		}
		}
	} catch (Exception e) {
		e.printStackTrace();
	}	
	return null;
}

public double  balanceCheck(int id){
	String sql="select balance from users where id=? ";
	try (PreparedStatement ps=con.prepareStatement(sql);)
	{
		ps.setInt(1,id);
		ResultSet rs=ps.executeQuery();
		rs.next();
		double balance=rs.getDouble("balance");
		return balance;
	} catch (Exception e) {
		e.printStackTrace();
	}
	return -1.0;
	
}

public void credit(int id, double amount) {
	String sql="UPDATE users SET balance = balance + ? WHERE id = ?;";
	try(PreparedStatement ps=con.prepareStatement(sql);)
	{
		ps.setDouble(1, amount);
		ps.setInt(2,id);
		ps.executeUpdate();
	} catch (Exception e) {
		e.printStackTrace();
	}
	
}

public void debit(int id, double amount) {
	String sql="UPDATE users SET balance = balance - ? WHERE id = ?;";
	try (PreparedStatement ps=con.prepareStatement(sql);)
		{
		ps.setDouble(1, amount);
		ps.setInt(2,id);
		ps.executeUpdate();
		ps.close();
		con.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
}
}
