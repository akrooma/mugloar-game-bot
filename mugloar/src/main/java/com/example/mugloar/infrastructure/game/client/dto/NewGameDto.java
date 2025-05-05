package com.example.mugloar.infrastructure.game.client.dto;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record NewGameDto(
        @NonNull
        String gameId,
        int lives,
        int gold,
        int level,
        int score,
        int highScore,
        int turn
) {
}
