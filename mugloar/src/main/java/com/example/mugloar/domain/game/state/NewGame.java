package com.example.mugloar.domain.game.state;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record NewGame(
        @NonNull
        String gameId,
        int lives,
        int gold,
        int level,
        int score,
        int turn
) {
}
