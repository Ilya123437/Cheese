package service;

import dataaccess.DataAccessException;
import dataaccess.User.UserDAO;
import dataaccess.Auth.AuthDAO;
import model.AuthData;
import model.UserData;
import service.Exceptions.AlreadyTakenException;
import service.Exceptions.BadRequestException;
import service.Exceptions.InvalidCredentialsException;
import service.Exceptions.UnauthorizedException;
import service.Records.LoginRequest;
import service.Records.LoginResult;
import service.Records.RegisterRequest;
import service.Records.RegisterResult;
import service.util.RequestValidator;
import java.util.Objects;
import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest request) throws DataAccessException, AlreadyTakenException, BadRequestException {
        // 400 status handling
        RequestValidator.validateRequiredFields(request.username(), request.password(), request.email());

        // 403 status handling
        if(userDAO.getUser(request.username()) != null) {
            throw new AlreadyTakenException("Username is already taken");
        }

        UserData user = new UserData(request.username(), request.password(), request.email());
        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(token, request.username());

        userDAO.createUser(user);
        authDAO.createAuth(auth);

        return new RegisterResult(request.username(), token);
    }

    public LoginResult login(LoginRequest request) throws BadRequestException, InvalidCredentialsException, DataAccessException {
        // 400 status handling
        RequestValidator.validateRequiredFields(request.username(), request.password());

        // 401 status handling
        if (userDAO.getUser(request.username()) == null) {
            throw new InvalidCredentialsException("User does not exist");
        } else if (!Objects.equals(userDAO.getUser(request.username()).password(), request.password())) {
            throw new InvalidCredentialsException("Password is incorrect");
        }

        // Generating session token
        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(token, request.username());
        authDAO.createAuth(auth);

        return new LoginResult(request.username(), token);
    }

    public void logout(String authToken) throws UnauthorizedException, DataAccessException{
        // 401 status handling
        if (authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedException("The token does not exist");
        }
        authDAO.deleteAuth(authToken);
    }
}
