package com.example.mugloar.domain.game.bot.strategy;

import com.example.mugloar.domain.game.bot.action.IncreaseHealthAction;
import com.example.mugloar.domain.game.bot.action.generic.Action;
import com.example.mugloar.domain.game.bot.strategy.generic.Strategy;
import com.example.mugloar.domain.game.shop.ShopItem;
import com.example.mugloar.domain.game.state.GameState;
import com.example.mugloar.domain.game.task.GameTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.mugloar.domain.game.bot.strategy.HealthUpkeepStrategy.getHealthPotion;
import static com.example.mugloar.domain.game.bot.strategy.generic.StrategyHelpers.shopItemIsTooExpensive;

/**
 * Attempts to bump out encrypted tasks by increasing health points.
 */
@Service
@RequiredArgsConstructor
public class BumpTasksStrategy implements Strategy {

    private static final Integer IS_ENCRYPTED = 1;

    private final IncreaseHealthAction increaseHealthAction;

    @Override
    public Optional<Action> evaluateGameState(GameState gameState) {
        if (thereAreNoEncryptedTasks(gameState.getGameTasks())) return Optional.empty();

        Optional<ShopItem> healthPotionOpt = getHealthPotion(gameState.getShopItems());

        if (healthPotionOpt.isEmpty() || shopItemIsTooExpensive(healthPotionOpt.get(), gameState.getGold())) {
            return Optional.empty();
        }

        return Optional.of(increaseHealthAction);
    }

    protected boolean thereAreNoEncryptedTasks(List<GameTask> gameTasks) {
        return gameTasks.stream().anyMatch(gameTask -> IS_ENCRYPTED.equals(gameTask.encrypted()));
    }

}
