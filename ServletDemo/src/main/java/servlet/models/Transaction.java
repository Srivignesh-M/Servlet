package servlet.Models;

import java.sql.Date;
import java.sql.Time;

public class Transaction {
	private int transactionId;
    private int fromUserId;
    private int toUserId;
    private double amount;
    private Date tDate;
    private Time tTime;
    private String type;
    
    public Transaction() {
    	super();
    }

	public Transaction(int i, int j, double d, String string) {
		super();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getTransactionId() {
		return transactionId;
	}
    
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	
	public int getFromUserId() {
		return fromUserId;
	}
	
	public void setFromUserId(int fromUserId) {
		this.fromUserId = fromUserId;
	}
	
	public int getToUserId() {
		return toUserId;
	}
	
	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public void settDate(Date tdate) {
		this.tDate = tdate;
	}
	
	public Date gettDate() {
		return tDate;
	}
	
	
	public Time gettTime() {
		return tTime;
	}
	
	public void settTime(Time tTime) {
		this.tTime = tTime;
	}

	
}
