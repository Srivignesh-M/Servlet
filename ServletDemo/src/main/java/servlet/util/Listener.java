package servlet.util;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@WebListener
public class Listener implements ServletContextListener {
	private static final Logger logger = LoggerFactory.getLogger(Listener.class);
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("starting liquidbase");
        try (Connection conn = DBConnection.getConnection()) {
            Liquibase liquibase = new Liquibase("db.changelog-master.xml",
                new ClassLoaderResourceAccessor(), new JdbcConnection(conn));
            liquibase.update("");
            logger.info("Liquibase Success: Database schema is synced for Jakarta app!");
        } catch (Exception e) {
            logger.error("Liquibase Error during startup: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Connection closed");
        DBConnection.shutdown(); 
    }
}