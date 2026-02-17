package com.example.servlet.Models;

public class User {
	    
		private String username;
	    private String email;
	    private String password;
	    private double balance;
	    private String role;
	    
	    public User() {
	    	
	    }
	    public User(int i, String string, String string2, double d) {
		    id=i;
		    email=string;
		    password=string2;
		    balance=d;
		}
	    public User(int id, String role) {
	    	this.id=id;
	    	this.role=role;
	    }
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}
		private int id;
	    public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public double getBalance() {
			return balance;
		}
		public void setBalance(double balance) {
			this.balance = balance;
		}
	}

