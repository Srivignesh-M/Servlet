package servlet.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import servlet.Models.Transaction;
import servlet.util.DBConnection;

public class TransactionDAO {

	public void createTransaction(int from_id, int to_id, double amount,String type) {
		String sql="Insert into transactions(from_user_id,to_user_id,amount,transaction_type) values(?,?,?,?);";
		try(Connection con=DBConnection.getConnection();
		PreparedStatement ps=con.prepareStatement(sql);){
		ps.setInt(1,from_id);
		ps.setInt(2,to_id);
		ps.setDouble(3,amount);
		ps.setString(4,type);
		ps.executeUpdate();
		ps.close();
		con.close();
		}
		catch(SQLException e) {
			System.err.println("SQL Error :"+e.getMessage());
		}
		catch(Exception e) {
			System.err.println("General Error :"+e.getMessage());
		}	
	}
	public ArrayList<Transaction> getTransactions(int id){
		Connection con;
		ArrayList<Transaction> transactions = new ArrayList<>();
		try {
			con = DBConnection.getConnection();
			String sql = "SELECT * FROM transactions WHERE from_user_id = ? ORDER BY t_date DESC, t_time DESC";
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				Transaction transaction = new Transaction();
				transaction.setTransactionId(rs.getInt("transaction_id"));
				transaction.setFromUserId(rs.getInt("from_user_id"));
				transaction.setToUserId(rs.getInt("to_user_id"));
				transaction.setType(rs.getString("transaction_type"));
				transaction.setAmount(rs.getDouble("amount"));
				transaction.settDate(rs.getDate("t_date"));
				transaction.settTime(rs.getTime("t_time"));
				transactions.add(transaction);
			}
			return transactions;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
