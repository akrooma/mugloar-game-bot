package com.example.mugloar.infrastructure.game.client.dto;

public record SolvedMessageDto(
        boolean success,
        int lives,
        int gold,
        int score,
        int highScore,
        int turn,
        String message
) {
}
