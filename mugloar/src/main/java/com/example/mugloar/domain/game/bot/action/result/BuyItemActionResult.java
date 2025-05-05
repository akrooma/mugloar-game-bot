package com.example.mugloar.domain.game.bot.action.result;

import com.example.mugloar.domain.game.bot.action.generic.ActionResult;
import com.example.mugloar.domain.game.shop.ShopItemReceipt;
import com.example.mugloar.domain.game.state.GameState;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BuyItemActionResult implements ActionResult {

    private final String itemId;

    private final ShopItemReceipt shopItemReceipt;

    @Override
    public void updateGameState(GameState gameState) {
        gameState.updateForUpgradePurchase(itemId, shopItemReceipt);
    }

}
