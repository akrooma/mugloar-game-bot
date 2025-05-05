package com.example.mugloar.infrastructure.game.client;

import com.example.mugloar.infrastructure.game.client.dto.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange
public interface GameClient {

    @PostExchange("/v2/game/start")
    NewGameDto startNewGame();

    @GetExchange("/v2/{gameId}/messages")
    List<MessageDto> getMessages(@PathVariable("gameId") String gameId);

    @PostExchange("/v2/{gameId}/solve/{adId}")
    SolvedMessageDto solveMessage(@PathVariable("gameId") String gameId, @PathVariable("adId") String adId);

    @GetExchange("/v2/{gameId}/shop")
    List<ShopItemDto> getShopItems(@PathVariable("gameId") String gameId);

    @PostExchange("/v2/{gameId}/shop/buy/{itemId}")
    ShopItemReceiptDto purchaseShopItem(@PathVariable("gameId") String gameId, @PathVariable("itemId") String itemId);

}
