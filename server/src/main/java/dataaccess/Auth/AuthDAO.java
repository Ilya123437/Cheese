package dataaccess.Auth;

import dataaccess.DataAccessException;
import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData auth) throws DataAccessException;
    AuthData getAuth(String username);
    void deleteAuth(String authToken);
    void clear() throws DataAccessException;
}
