package com.example.mugloar.domain.game.bot.action;

import com.example.mugloar.domain.game.GameService;
import com.example.mugloar.domain.game.bot.action.generic.Action;
import com.example.mugloar.domain.game.bot.action.generic.ActionResult;
import com.example.mugloar.domain.game.bot.action.result.BuyItemActionResult;
import com.example.mugloar.domain.game.shop.ShopItemReceipt;
import com.example.mugloar.domain.game.state.GameState;
import lombok.RequiredArgsConstructor;

/**
 * Tries to buy an item from the shop.
 */
@RequiredArgsConstructor
public class BuyItemAction implements Action {

    private final GameService gameService;

    private final String itemId;

    @Override
    public ActionResult execute(GameState gameState) {
        ShopItemReceipt shopItemReceipt = gameService.tryBuyShopItem(gameState.getGameId(), itemId);

        return new BuyItemActionResult(itemId, shopItemReceipt);
    }

}
