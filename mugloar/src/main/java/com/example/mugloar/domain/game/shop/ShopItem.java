package com.example.mugloar.domain.game.shop;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record ShopItem(
        @NonNull
        String id,
        int cost
) {
}
