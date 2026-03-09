package servlet.DAO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

import servlet.Models.Transaction;
import servlet.util.DBConnection;

class TransactionDAOTest {
	private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);
    @Mock private Connection mockConnection;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private ResultSet mockResultSet;

    private TransactionDAO transactionDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionDAO = new TransactionDAO();
    }
    @Test
    void testCreateSuccess() {
        
        try (MockedConstruction<HikariDataSource> mockedHikari = mockConstruction(HikariDataSource.class);
             MockedStatic<DBConnection> mockedDb = mockStatic(DBConnection.class)) {
        	when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            mockedDb.when(DBConnection::getConnection).thenReturn(mockConnection);
            transactionDAO.createTransaction(1, 2, 500.00, "Credit");
            verify(mockPreparedStatement, times(1)).executeUpdate();
        }
        catch(Exception e){
        	logger.info("SQL Error ",e);
        }
    }

    @Test
    void testCreateFailed(){
        try (MockedConstruction<HikariDataSource> mockedHikari = mockConstruction(HikariDataSource.class);
             MockedStatic<DBConnection> mockedDb = mockStatic(DBConnection.class)) {
            mockedDb.when(DBConnection::getConnection).thenThrow(new SQLException("Database Error"));
            transactionDAO.createTransaction(1, 2, 500.00, "Debit");
            verify(mockPreparedStatement, never()).executeUpdate();
        }
        catch(Exception e){
        	logger.info("SQL Error ",e);
        }
    }
    @Test
    void testGetTransactionsSuccess(){
        try (MockedConstruction<HikariDataSource> mockedHikari = mockConstruction(HikariDataSource.class);
             MockedStatic<DBConnection> mockedDb = mockStatic(DBConnection.class)) {
        	when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getInt("transaction_id")).thenReturn(101);
            mockedDb.when(DBConnection::getConnection).thenReturn(mockConnection);
            ArrayList<Transaction> transactions = transactionDAO.getTransactions(1, 0);
            assertEquals(1, transactions.size());
            assertEquals(101, transactions.get(0).getTransactionId());
        }
        catch(Exception e){
        	logger.info("SQL Error ",e);
        }
    }

    @Test
    void testGetTransactionsFailed() {
        try (MockedConstruction<HikariDataSource> mockedHikari = mockConstruction(HikariDataSource.class);
             MockedStatic<DBConnection> mockedDb = mockStatic(DBConnection.class)) {
            mockedDb.when(DBConnection::getConnection).thenThrow(new SQLException("Database Error"));
            ArrayList<Transaction> transactions = transactionDAO.getTransactions(1, 0);
            assertNull(transactions);
        }
        catch(Exception e){
        	logger.info("SQL Error ",e);
        }
    }
}