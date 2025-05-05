package com.example.mugloar.infrastructure.game.client.dto;

public record ShopItemReceiptDto(
        boolean shoppingSuccess,
        int gold,
        int lives,
        int level,
        int turn
) {
}
