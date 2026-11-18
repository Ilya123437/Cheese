package dataaccess.game;

import dataaccess.DataAccessException;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void createGame(GameData data) throws DataAccessException;
    GameData getGame(Integer id) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    void updateGame(Integer id, GameData data) throws DataAccessException;
    void clear() throws DataAccessException;
}
