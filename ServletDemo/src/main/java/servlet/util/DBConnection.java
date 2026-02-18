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
	private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);
	private static HikariDataSource dataSource;
	 private static String URL;
	    private static String USER;
	    private static String PASS;
	static {
        try (InputStream input = DBConnection.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            Properties prop = new Properties();         
            prop.load(input);
            URL = prop.getProperty("URL");
            USER = prop.getProperty("USER");
            PASS = prop.getProperty("PASS"); 
            logger.info("properties loaded");
        } catch (Exception e) {
        	logger.error("Failed to load properties", e.getMessage(), e);
        }
HikariConfig config = new HikariConfig();     
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASS);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setIdleTimeout(300000);
        config.setConnectionTimeout(20000);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
    }
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            logger.info("Closing HikariCP DataSource...");
            dataSource.close();
            logger.info("HikariCP DataSource closed successfully.");
        }
    }
}