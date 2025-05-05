package com.example.mugloar.domain.game.bot.strategy;

import com.example.mugloar.common.constants.ItemId;
import com.example.mugloar.domain.game.bot.action.generic.Action;
import com.example.mugloar.domain.game.bot.action.IncreaseHealthAction;
import com.example.mugloar.domain.game.bot.strategy.generic.Strategy;
import com.example.mugloar.domain.game.shop.ShopItem;
import com.example.mugloar.domain.game.state.GameState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.mugloar.domain.game.bot.strategy.generic.StrategyHelpers.shopItemIsTooExpensive;

/**
 * Keep the player "healthy" if
 * necessary (lives count is critical)
 * and possible (player can buy a health potion).
 */
@Service
@RequiredArgsConstructor
public class HealthUpkeepStrategy implements Strategy {

    protected static final String HEALTH_POTION_ID = ItemId.HPOT.getValue();

    private final IncreaseHealthAction increaseHealthAction;

    @Override
    public Optional<Action> evaluateGameState(GameState gameState) {
        if (gameState.playerIsHealthy()) return Optional.empty();

        Optional<ShopItem> healthPotionOpt = getHealthPotion(gameState.getShopItems());

        if (healthPotionOpt.isEmpty() || shopItemIsTooExpensive(healthPotionOpt.get(), gameState.getGold())) {
            return Optional.empty();
        }

        return Optional.of(increaseHealthAction);
    }

    protected static Optional<ShopItem> getHealthPotion(List<ShopItem> availableShopItems) {
        return availableShopItems.stream()
                .filter(shopItem -> shopItem.id().equals(HEALTH_POTION_ID))
                .findFirst();
    }

}
