package com.example.mugloar.infrastructure.game.client.dto;

import lombok.NonNull;

public record ShopItemDto(
        @NonNull
        String id,
        String name,
        int cost
) { }
