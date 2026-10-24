package service.Records;

import org.jetbrains.annotations.NotNull;

public record JoinGameRequest(
        String playerColor,
        @NotNull Integer gameID
) {
}
