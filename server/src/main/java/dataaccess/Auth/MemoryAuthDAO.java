package dataaccess.Auth;

import dataaccess.DataAccessException;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO{
    private final Map<String, AuthData> authMemory = new HashMap<>();

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        if (authMemory.containsKey(auth.authToken())) {
            throw new DataAccessException("Auth token already exists");
        }
        authMemory.put(auth.authToken(), auth);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return authMemory.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        authMemory.remove(authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        authMemory.clear();
    }
}
