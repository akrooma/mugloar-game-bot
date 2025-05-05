package com.example.mugloar.domain.game.bot.action;

import com.example.mugloar.common.constants.ItemId;
import com.example.mugloar.domain.game.GameService;
import com.example.mugloar.domain.game.bot.action.generic.Action;
import com.example.mugloar.domain.game.bot.action.generic.ActionResult;
import com.example.mugloar.domain.game.bot.action.result.IncreaseHealthActionResult;
import com.example.mugloar.domain.game.shop.ShopItemReceipt;
import com.example.mugloar.domain.game.state.GameState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Tries to increase lives by buying a health potion.
 */
@Service
@RequiredArgsConstructor
public class IncreaseHealthAction implements Action {

    private final GameService gameService;

    @Override
    public ActionResult execute(GameState gameState) {
        ShopItemReceipt shopItemReceipt = gameService.tryBuyShopItem(gameState.getGameId(), ItemId.HPOT.getValue());

        return new IncreaseHealthActionResult(shopItemReceipt);
    }

}
