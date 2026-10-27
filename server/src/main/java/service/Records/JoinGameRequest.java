package service.Records;

public record JoinGameRequest(
        String playerColor,
        Integer gameID
) {
}
