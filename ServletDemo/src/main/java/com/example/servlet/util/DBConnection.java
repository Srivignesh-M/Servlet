package com.example.servlet.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {
	 private static String URL;
	    private static String USER;
	    private static String PASS;
	static {
        try (InputStream input = SecurityUtil.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            Properties prop = new Properties();         
            prop.load(input);
            URL= prop.getProperty("URL");
            USER = prop.getProperty("USER");
            PASS = prop.getProperty("PASS"); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver"); 
        return DriverManager.getConnection(URL, USER, PASS);
    }
}