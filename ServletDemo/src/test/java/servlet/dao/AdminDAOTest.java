package servlet.DAO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariDataSource;

import servlet.Models.User;
import servlet.util.DBConnection;
import servlet.util.SecurityUtil;

class AdminDAOTest {
	private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);
    @Mock private Connection mockConnection;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private ResultSet mockResultSet;

    private AdminDAO adminDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminDAO = new AdminDAO();
    }

    @Test
    void testGetUsersSuccess(){        
        try (MockedConstruction<HikariDataSource> mockedHikari = mockConstruction(HikariDataSource.class);
             MockedStatic<DBConnection> mockedDb = mockStatic(DBConnection.class);
             MockedStatic<SecurityUtil> mockedSecurity = mockStatic(SecurityUtil.class)) {  
        	when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true, true, false);
            when(mockResultSet.getInt("id")).thenReturn(1, 2);
            when(mockResultSet.getString("username")).thenReturn("srivignesh", "john_doe");
            when(mockResultSet.getString("email")).thenReturn("encrypted_email_1", "encrypted_email_2");
            when(mockResultSet.getDouble("balance")).thenReturn(1500.00, 250.50);
            mockedDb.when(DBConnection::getConnection).thenReturn(mockConnection);
            mockedSecurity.when(() -> SecurityUtil.decrypt("encrypted_email_1")).thenReturn("srivignesh@example.com");
            mockedSecurity.when(() -> SecurityUtil.decrypt("encrypted_email_2")).thenReturn("john@example.com");
            ArrayList<User> users = adminDAO.getUsers();
            assertEquals(2, users.size());
            assertEquals(1, users.get(0).getId());
            assertEquals("srivignesh", users.get(0).getUsername());
            assertEquals("srivignesh@example.com", users.get(0).getEmail());
            assertEquals(1500.00, users.get(0).getBalance());
            assertEquals(2, users.get(1).getId());
            assertEquals("john_doe", users.get(1).getUsername());
        }
        catch(Exception e) {
        	logger.info("SQL Error ",e);
        }
    }

    @Test
    void testGetUsersFailed() {
        try (MockedConstruction<HikariDataSource> mockedHikari = mockConstruction(HikariDataSource.class);
             MockedStatic<DBConnection> mockedDb = mockStatic(DBConnection.class)) {
            mockedDb.when(DBConnection::getConnection).thenThrow(new SQLException("Database offline"));
            ArrayList<User> users = adminDAO.getUsers();
            assertTrue(users.isEmpty());
        }
        catch(Exception e) {
        	logger.info("SQL Error ",e);
        }
    }
}