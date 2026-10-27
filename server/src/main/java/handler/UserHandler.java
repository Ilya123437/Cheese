package handler;

import com.google.gson.Gson;
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
    private final Gson serializer = new Gson();

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public void register(Context ctx) {
        try {
            RegisterRequest request = serializer.fromJson(ctx.body(),RegisterRequest.class);
            RegisterResult result = userService.register(request);
            ctx.status(200);
            ctx.json(serializer.toJson(result));
        } catch (BadRequestException error) {
            ctx.status(400);
            ctx.result(String.format("""
                {"message": "%s"}""",error.getMessage()));
        } catch (AlreadyTakenException error) {
            ctx.status(403);
            ctx.result(String.format("""
                {"message": "%s"}""",error.getMessage()));
        } catch (DataAccessException error) {
            ctx.status(500);
            ctx.result(String.format("""
                {"message": "%s"}""",error.getMessage()));
        }
    }
}
