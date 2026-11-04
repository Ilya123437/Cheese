package dataaccess.Game;

import dataaccess.DataAccessException;
import model.GameData;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO {
    private final Map<Integer, GameData> gameMemory = new HashMap<>();



    public void createGame(GameData data) throws DataAccessException {
        if (gameMemory.containsKey(data.gameID())) {
            throw new DataAccessException("Game id already exists");
        }
        gameMemory.put(data.gameID(), data);
    }


    public GameData getGame(Integer id) {
        return gameMemory.get(id);
    }


    public Collection<GameData> listGames() {
        return gameMemory.values();
    }


    public void updateGame(Integer id, GameData data) throws DataAccessException{
        if (!gameMemory.containsKey(id)) {
            throw new DataAccessException("Game id does not exist");
        }
        gameMemory.put(id, data);
    }


    public void clear() throws DataAccessException {
        gameMemory.clear();
    }
}
