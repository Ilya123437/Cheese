package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.exceptions.AlreadyTakenException;
import service.exceptions.BadRequestException;
import service.records.RegisterRequest;
import io.javalin.http.Context;
import service.records.RegisterResult;
import service.UserService;


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
