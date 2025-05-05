package com.example.mugloar.domain.game.bot.strategy;

import com.example.mugloar.domain.game.GameService;
import com.example.mugloar.domain.game.bot.action.BuyItemAction;
import com.example.mugloar.domain.game.bot.action.generic.Action;
import com.example.mugloar.domain.game.bot.strategy.generic.Strategy;
import com.example.mugloar.domain.game.shop.ShopItem;
import com.example.mugloar.domain.game.state.GameState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.example.mugloar.domain.game.bot.strategy.generic.StrategyHelpers.shopItemIsTooExpensive;

/**
 * Buy a missing upgrade item if the player has enough money.
 * Buy the cheapest item if the player has all available items (TODO: separate strategy for this?).
 */
@Service
@RequiredArgsConstructor
public class LevelUpStrategy implements Strategy {

    private final GameService gameService;

    @Override
    public Optional<Action> evaluateGameState(GameState gameState) {
        return findPossibleUpgradeItem(gameState)
                .map(shopItem  -> new BuyItemAction(gameService, shopItem.id()));
    }

    protected static int getTotalBudget(GameState gameState) {
        Optional<ShopItem> healthPotionOpt = HealthUpkeepStrategy.getHealthPotion(gameState.getShopItems());

        return healthPotionOpt
                .map(shopItem -> gameState.getGold() - shopItem.cost())
                .orElseGet(gameState::getGold);
    }

    protected static Optional<ShopItem> findPossibleUpgradeItem(GameState gameState) {
        int totalBudget = getTotalBudget(gameState);

        return findBestPossibleUpgrade(gameState, totalBudget);
    }

    protected static Optional<ShopItem> findBestPossibleUpgrade(GameState gameState, int totalBudget) {
        List<ShopItem> upgradeItems = filterAndOrderUpgradeItems(gameState.getShopItems());

        Optional<ShopItem> cheapestUpgradeOpt = findCheapestUpgrade(gameState, upgradeItems, totalBudget);

        if (cheapestUpgradeOpt.isPresent()) return cheapestUpgradeOpt;

        return saveMoneyForAnUpgrade(gameState, upgradeItems)
                ? Optional.empty()
                : findCheapestItem(upgradeItems, totalBudget);
    }

    /**
     * Returns a list of shop items with no health potions.
     * Items are ordered by their cost in ascending order.
     * @param shopItems shop items to filter and order
     * @return filtered and ordered shop items
     */
    protected static List<ShopItem> filterAndOrderUpgradeItems(List<ShopItem> shopItems) {
        return shopItems.stream()
                .filter(shopItem -> !shopItem.id().equals(HealthUpkeepStrategy.HEALTH_POTION_ID))
                .sorted(Comparator.comparingInt(ShopItem::cost))
                .toList();
    }

    protected static Optional<ShopItem> findCheapestUpgrade(GameState gameState, List<ShopItem> upgradeItems, int totalBudget) {
        for (ShopItem upgradeItem : upgradeItems) {
            if (gameState.hasItem(upgradeItem.id()) || shopItemIsTooExpensive(upgradeItem, totalBudget)) {
                continue;
            }

            return Optional.of(upgradeItem);
        }

        return Optional.empty();
    }

    protected static Optional<ShopItem> findCheapestItem(List<ShopItem> upgradeItems, int totalBudget) {
        for (ShopItem upgradeItem : upgradeItems) {
            if (shopItemIsTooExpensive(upgradeItem, totalBudget)) continue;

            return Optional.of(upgradeItem);
        }

        return Optional.empty();
    }

    private static boolean saveMoneyForAnUpgrade(GameState gameState, List<ShopItem> upgradeItems) {
        for (ShopItem upgradeItem : upgradeItems) {
            if (gameState.hasItem(upgradeItem.id())) continue;

            return true;
        }

        return false;
    }

}
