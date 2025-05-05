package com.example.mugloar.domain.game.bot.strategy.generic;

import com.example.mugloar.domain.game.bot.strategy.*;

import java.util.List;

public final class StrategyConstants {

    private StrategyConstants() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    /**
     * Strategy evaluation execution order.
     */
    public static final List<Class<? extends Strategy>> STRATEGY_ORDER = List.of(
            HealthUpkeepStrategy.class,
            SafeTaskStrategy.class,
            LevelUpStrategy.class,
            EasyTaskStrategy.class
    );

}
