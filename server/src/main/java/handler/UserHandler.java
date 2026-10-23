package handler;

import dataaccess.DataAccessException;
import service.Exceptions.AlreadyTakenException;
import service.Exceptions.BadRequestException;
import service.Records.RegisterRequest;
import io.javalin.http.Context;
import service.Records.RegisterResult;
import service.UserService;
import java.util.Map;

public class UserHandler {
    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public void register(Context ctx) {
        try {
            RegisterRequest request = ctx.bodyAsClass(RegisterRequest.class);

            RegisterResult result = userService.register(new RegisterRequest(
                    request.username(),
                    request.password(),
                    request.email()
            ));

            ctx.status(200);
            ctx.json(result);

        } catch (BadRequestException error) {
            ctx.status(400);
            ctx.json(Map.of("message", error.getMessage()));
        } catch (AlreadyTakenException error) {
            ctx.status(403);
            ctx.json(Map.of("message", error.getMessage()));
        } catch (DataAccessException error) {
            ctx.status(500);
            ctx.json(Map.of("message", error.getMessage()));
        }
    }


}
