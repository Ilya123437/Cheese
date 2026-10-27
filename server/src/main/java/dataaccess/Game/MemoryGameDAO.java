package dataaccess.Game;

import dataaccess.DataAccessException;
import model.GameData;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{
    private final Map<Integer, GameData> gameMemory = new HashMap<>();


    @Override
    public void createGame(GameData data) throws DataAccessException {
        if (gameMemory.containsKey(data.gameID())) {
            throw new DataAccessException("Game id already exists");
        }
        gameMemory.put(data.gameID(), data);
    }

    @Override
    public GameData getGame(Integer id) {
        return gameMemory.get(id);
    }

    @Override
    public Collection<GameData> listGames() {
        return gameMemory.values();
    }

    @Override
    public void updateGame(Integer id, GameData data) throws DataAccessException{
        if (!gameMemory.containsKey(id)) {
            throw new DataAccessException("Game id does not exist");
        }
        gameMemory.put(id, data);
    }

    @Override
    public void clear() throws DataAccessException {
        gameMemory.clear();
    }
}
