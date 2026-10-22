package service.Records;

import model.GameData;

import java.util.Collection;

public record ListGamesResult(
        Collection<GameData> listGames
) {
}
