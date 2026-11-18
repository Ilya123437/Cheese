package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.exceptions.BadRequestException;
import service.exceptions.InvalidCredentialsException;
import service.exceptions.UnauthorizedException;
import service.records.LoginRequest;
import service.records.LoginResult;
import service.UserService;

public class SessionHandler {
    private final UserService userService;
    private final Gson serializer = new Gson();

    public SessionHandler(UserService userService) {
        this.userService = userService;
    }

    public void login(Context ctx) {
        try {
            LoginRequest request = serializer.fromJson(ctx.body(),LoginRequest.class);
            LoginResult result = userService.login(request);
            ctx.status(200);
            ctx.json(serializer.toJson(result));
        } catch (BadRequestException error) {
            ctx.status(400);
            ctx.result(String.format("""
                {"message": "%s"}""",error.getMessage()));
        } catch (InvalidCredentialsException error) {
            ctx.status(401);
            ctx.result(String.format("""
                {"message": "%s"}""",error.getMessage()));
        } catch (DataAccessException error) {
            ctx.status(500);
            ctx.result(String.format("""
                {"message": "%s"}""",error.getMessage()));
        }
    }

    public void logout(Context ctx) {
        String authToken = ctx.header("Authorization");

        try {
            userService.logout(authToken);
            ctx.status(200);
        } catch (UnauthorizedException error) {
            ctx.status(401);
            ctx.result(String.format("""
                {"message": "%s"}""",error.getMessage()));
        } catch (DataAccessException error) {
            ctx.status(500);
            ctx.result(String.format("""
                {"message": "%s"}""",error.getMessage()));
        }
    }
}
