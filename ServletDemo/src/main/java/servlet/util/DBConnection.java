package servlet.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBConnection {
	private static final Logger LOGGER = LoggerFactory.getLogger(DBConnection.class);
	private static HikariDataSource dataSource;
	 private static String url;
	    private static String user;
	    private static String pass;
	static {
        try (InputStream input = DBConnection.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            Properties prop = new Properties();         
            prop.load(input);
            url = prop.getProperty("URL");
            user = prop.getProperty("USER");
            pass = prop.getProperty("PASS"); 
            LOGGER.info("properties loaded");
        } catch (Exception e) {
        	LOGGER.error("Failed to load properties", e.getMessage(), e);
        }
HikariConfig config = new HikariConfig();     
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(pass);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(10);
        config.setIdleTimeout(1000*60*60);
        config.setConnectionTimeout(1000*20);
        dataSource = new HikariDataSource(config);
    }
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            LOGGER.info("Closing HikariCP");
            dataSource.close();
            LOGGER.info("HikariCP DataSource closed successfully.");
        }
    }
}