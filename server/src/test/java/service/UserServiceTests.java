package service;

import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import dataaccess.User.MemoryUserDAO;
import dataaccess.Auth.MemoryAuthDAO;
import service.Records.RegisterRequest;
import service.Records.RegisterResult;

public class UserServiceTests {
    private final UserService service = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());

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
