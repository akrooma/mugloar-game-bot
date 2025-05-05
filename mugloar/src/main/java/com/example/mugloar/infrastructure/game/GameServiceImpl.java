package com.example.mugloar.infrastructure.game;

import com.example.mugloar.domain.game.GameService;
import com.example.mugloar.domain.game.shop.ShopItem;
import com.example.mugloar.domain.game.shop.ShopItemReceipt;
import com.example.mugloar.domain.game.state.GameState;
import com.example.mugloar.domain.game.task.CompletedGameTask;
import com.example.mugloar.domain.game.task.GameTask;
import com.example.mugloar.infrastructure.game.client.GameClient;
import com.example.mugloar.infrastructure.game.client.dto.*;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameClient gameClient;

    private final GameMapper gameMapper = Mappers.getMapper(GameMapper.class);

    @Override
    public GameState startNewGame() {
        NewGameDto newGameDto = gameClient.startNewGame();
        List<GameTask> gameTasks = this.getAvailableTasks(newGameDto.gameId());
        List<ShopItem> shopItems = this.getAvailableShopItems(newGameDto.gameId());

        return new GameState(gameMapper.map(newGameDto), gameTasks, shopItems);
    }

    @Override
    public List<GameTask> getAvailableTasks(String gameId) {
        List<MessageDto> availableMessages = gameClient.getMessages(gameId);

        return gameMapper.mapMessages(availableMessages);
    }

    @Override
    public CompletedGameTask tryCompleteTask(String gameId, String taskId) {
        SolvedMessageDto solvedMessage = gameClient.solveMessage(gameId, taskId);

        return gameMapper.map(solvedMessage);
    }

    @Override
    public List<ShopItem> getAvailableShopItems(String gameId) {
        List<ShopItemDto> availableShopItems = gameClient.getShopItems(gameId);

        return gameMapper.mapItems(availableShopItems);
    }

    @Override
    public ShopItemReceipt tryBuyShopItem(String gameId, String itemId) {
        ShopItemReceiptDto shopItemReceipt = gameClient.purchaseShopItem(gameId, itemId);

        return gameMapper.map(shopItemReceipt);
    }

}
