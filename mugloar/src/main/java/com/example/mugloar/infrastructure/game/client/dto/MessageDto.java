package com.example.mugloar.infrastructure.game.client.dto;

import lombok.NonNull;

public record MessageDto(
        @NonNull
        String adId,
        String message,
        int reward,
        int expiresIn,
        Integer encrypted,
        String probability
) {
}
