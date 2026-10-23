package handler;

import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.Exceptions.BadRequestException;
import service.Exceptions.InvalidCredentialsException;
import service.Records.LoginRequest;
import service.Records.LoginResult;
import service.UserService;

import java.util.Map;

public class SessionHandler {

    private final UserService userService;

    public SessionHandler(UserService userService) {
        this.userService = userService;
    }

    public void login(Context ctx) {
        try {
            LoginRequest request = ctx.bodyAsClass(LoginRequest.class);

            LoginResult result = userService.login(new LoginRequest(
                    request.username(),
                    request.password()
            ));

            ctx.status(200);
            ctx.json(result);
        } catch (BadRequestException error) {
            ctx.status(400);
            ctx.json(Map.of("message", error.getMessage()));
        } catch (InvalidCredentialsException error) {
            ctx.status(401);
            ctx.json(Map.of("message", error.getMessage()));
        } catch (DataAccessException error) {
            ctx.status(500);
            ctx.json(Map.of("message", error.getMessage()));
        }
    }

    public void logout(Context ctx) {
        String authToken = ctx.header("Authorization");

        try {
            userService.logout(authToken);
        } catch (DataAccessException error) {
            ctx.status(500);
            ctx.json(Map.of("message", error.getMessage()));
        }
    }
}
