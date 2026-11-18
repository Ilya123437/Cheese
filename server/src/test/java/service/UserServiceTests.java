package service;

import dataaccess.sql.MySqlAuthDAO;
import dataaccess.sql.MySqlUserDAO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import service.records.RegisterRequest;
import service.records.RegisterResult;

public class UserServiceTests {
    private final UserService service = new UserService(new MySqlUserDAO(), new MySqlAuthDAO());

    @Test
    public void registerSuccess() throws Exception {
        RegisterRequest request = new RegisterRequest("testUsername", "testPassword", "testEmail@gmail.com");
        RegisterResult result = service.register(request);

        assertEquals("testUsername", result.username());
        assertNotNull(result.authToken());
        assertFalse(result.authToken().isBlank());
    }

    @Test
    public void registerDuplicateUser() throws Exception {

    }
}
