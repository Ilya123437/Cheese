package service.records;

public record JoinGameRequest(
        String playerColor,
        Integer gameID
) {
}
