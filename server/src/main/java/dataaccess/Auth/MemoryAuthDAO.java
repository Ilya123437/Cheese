package dataaccess.Auth;

import dataaccess.DataAccessException;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO {
    private final Map<String, AuthData> authMemory = new HashMap<>();


    public void createAuth(AuthData auth) throws DataAccessException {
        if (authMemory.containsKey(auth.authToken())) {
            throw new DataAccessException("Auth token already exists");
        }
        authMemory.put(auth.authToken(), auth);
    }

    public AuthData getAuth(String authToken) {
        return authMemory.get(authToken);
    }

    public void deleteAuth(String authToken) {
        authMemory.remove(authToken);
    }

    public void clear() throws DataAccessException {
        authMemory.clear();
    }
}
