package servlet.DAO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import servlet.Models.User;

class UserDAOTest {
    private UserDAO userDAO;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    void setUp() throws SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        userDAO = new UserDAO(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }
    @Test
    @DisplayName("Register - Success: Should return true when DB update succeeds")
    void testRegisterUser_Success() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(1);
        assertDoesNotThrow(() -> userDAO.registerUser("vignesh", "v@mail.com", "pass123", "USER"));
        verify(preparedStatement, times(1)).executeUpdate();
        
    }

    @Test
    void testRegister_Failure() throws SQLException {
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException());
        assertThrows(Exception.class, () -> {
            userDAO.registerUser("vicky", "v@m.com", "pw", "USER");
        });
    }
    @Test
    void testLoginSuccess() throws SQLException {
    	String hashedPass = servlet.util.PasswordUtil.hashPassword("plain_pass");
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("role")).thenReturn("USER");
        when(resultSet.getString("password")).thenReturn(hashedPass);
        User user = userDAO.login("test@mail.com", "plain_pass");

        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("USER", user.getRole());
    }

    @Test
    @DisplayName("Login - Failure: Should return null when user not found")
    void testLoginUserNotFound() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        User user = userDAO.login("wrong@mail.com", "any");

        assertNull(user);
    }

    @Test
    @DisplayName("Balance - Success: Should return exact balance from DB")
    void testBalanceCheck_Success() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getDouble("balance")).thenReturn(2500.75);

        double balance = userDAO.balanceCheck(101);
        assertEquals(2500.75,balance);
        
    }

    @Test
    @DisplayName("Balance - Failure: Should return -1.0 on SQL error")
    void testBalanceCheck_Failure() throws SQLException {
        when(preparedStatement.executeQuery()).thenThrow(new SQLException("Connection Lost"));
        double balance = userDAO.balanceCheck(101);
        assertEquals(-1.0, balance);
    }
    @Test
    @DisplayName("Credit - Success: Should execute update without throwing exception")
    void testCredit_Success() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(1);
        assertDoesNotThrow(() -> userDAO.credit(101, 500.0));
        verify(preparedStatement, times(1)).setDouble(1, 500.0);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    @DisplayName("Credit - Failure: Should handle SQLException internally")
    void testCredit_Failure() throws SQLException {
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("DB Error"));
        assertDoesNotThrow(() -> userDAO.credit(101, 500.0));
        verify(preparedStatement).executeUpdate();
    }

    @Test
    @DisplayName("Debit - Success: Should verify parameters are set correctly")
    void testDebit_Success() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> userDAO.debit(101, 200.0));
        verify(preparedStatement).setDouble(1, 200.0);
        verify(preparedStatement).setInt(2, 101);
        verify(preparedStatement).executeUpdate();
        
    }

    @Test
    @DisplayName("Debit - Failure: Should complete even if DB fails")
    void testDebit_Failure() throws SQLException {
        doThrow(new SQLException()).when(preparedStatement).executeUpdate();

        assertDoesNotThrow(() -> userDAO.debit(101, 200.0));
    }
    
}