package com.example.mugloar.domain.game.bot.strategy.generic;

import com.example.mugloar.domain.game.shop.ShopItem;
import com.example.mugloar.domain.game.task.GameTask;

public final class StrategyHelpers {

    private StrategyHelpers() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static boolean shopItemIsTooExpensive(ShopItem shopItem, int totalGold) {
        return shopItem.cost() > totalGold;
    }

    public static int calculateTaskScore(GameTask gameTask) {
        return gameTask.reward() * gameTask.taskSuccessProbability().getValue();
    }

}
