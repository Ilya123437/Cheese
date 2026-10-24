package dataaccess.Game;

import dataaccess.DataAccessException;
import model.GameData;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface GameDAO {
    void createGame(GameData data) throws DataAccessException;
    GameData getGame(Integer id);
    Collection<GameData> listGames();
    void updateGame(Integer id, GameData data) throws DataAccessException;
    void clear() throws DataAccessException;
}
