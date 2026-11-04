package dataaccess;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTests {
    @BeforeAll
    static void setup() throws DataAccessException {
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();
    }

    @Test
    void getConnectionTest() throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            assertNotNull(conn);
        }
    }
}
