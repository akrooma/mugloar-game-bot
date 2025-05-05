package com.example.mugloar.domain.game;

import com.example.mugloar.domain.game.shop.ShopItem;
import com.example.mugloar.domain.game.shop.ShopItemReceipt;
import com.example.mugloar.domain.game.state.GameState;
import com.example.mugloar.domain.game.task.CompletedGameTask;
import com.example.mugloar.domain.game.task.GameTask;

import java.util.List;

public interface GameService {

    GameState startNewGame();

    List<GameTask> getAvailableTasks(String gameId);

    CompletedGameTask tryCompleteTask(String gameId, String taskId);

    List<ShopItem> getAvailableShopItems(String gameId);

    ShopItemReceipt tryBuyShopItem(String gameId, String itemId);

}
