package dataaccess.User;

import dataaccess.DataAccessException;
import model.UserData;

public interface UserDAO {
    void createUser(UserData user) throws DataAccessException;
    UserData getUser(String username);
    void clear();

}
