package service;

import dataaccess.DataAccessException;
import dataaccess.User.UserDAO;
import dataaccess.Auth.AuthDAO;
import model.AuthData;
import model.UserData;
import org.jetbrains.annotations.NotNull;
import service.Exceptions.AlreadyTakenException;
import service.Exceptions.InvalidCredentialsException;
import service.Exceptions.UnauthorizedException;
import service.Records.LoginResult;
import service.Records.RegisterResult;
import java.util.Objects;
import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(String username, String password, String email) throws DataAccessException, AlreadyTakenException {
        if(userDAO.getUser(username) != null) {
            throw new AlreadyTakenException("Username is already taken");
        }
        UserData user = new UserData(username, password, email);

        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(token, username);

        userDAO.createUser(user);
        authDAO.createAuth(auth);

        return new RegisterResult(username, token);
    }

    public LoginResult login(@NotNull String username, @NotNull String password) throws InvalidCredentialsException, DataAccessException {
        if (userDAO.getUser(username) == null) {
            throw new InvalidCredentialsException("User does not exist");
        } else if (!Objects.equals(userDAO.getUser(username).password(), password)) {
            throw new InvalidCredentialsException("Password is incorrect");
        }
        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(token, username);
        authDAO.createAuth(auth);

        return new LoginResult(username, token);
    }

    public void logout(String authToken) throws UnauthorizedException, DataAccessException{
        if (authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedException("The token does not exist");
        }
        authDAO.deleteAuth(authToken);
    }
}
