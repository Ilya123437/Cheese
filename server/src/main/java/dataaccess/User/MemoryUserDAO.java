package dataaccess.User;
import dataaccess.DataAccessException;
import model.UserData;


import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    private final Map<String, UserData> userMemory = new HashMap<>();

    @Override
    public void createUser(UserData user) throws DataAccessException{
        userMemory.put(user.username(), user);
    }

    @Override
    public UserData getUser(String username) {
        return userMemory.get(username);
    }

    @Override
    public void clear() throws DataAccessException{
        userMemory.clear();
    }
}
