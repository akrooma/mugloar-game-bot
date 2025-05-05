package com.example.mugloar.domain.game.task;

import lombok.Builder;

@Builder
public record CompletedGameTask(
        boolean success,
        int lives,
        int gold,
        int score,
        int turn
) {
}
