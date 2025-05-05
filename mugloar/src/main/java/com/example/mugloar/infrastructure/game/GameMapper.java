package com.example.mugloar.infrastructure.game;

import com.example.mugloar.common.constants.TaskSuccessProbability;
import com.example.mugloar.domain.game.shop.ShopItem;
import com.example.mugloar.domain.game.shop.ShopItemReceipt;
import com.example.mugloar.domain.game.state.NewGame;
import com.example.mugloar.domain.game.task.CompletedGameTask;
import com.example.mugloar.domain.game.task.GameTask;
import com.example.mugloar.infrastructure.game.client.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface GameMapper {

//    @Mapping(source = "tasks", target = "availableGameTasks")
//    @Mapping(source = "shopItems", target = "availableShopItems")
//    GameState map(NewGameDto newGame, List<GameTask> tasks, List<ShopItem> shopItems);
    NewGame map(NewGameDto newGame);

    @Mapping(source = "adId", target = "taskId")
    @Mapping(source = "probability", target = "taskSuccessProbability", qualifiedByName = "mapProbability")
    GameTask map(MessageDto messages);

    ShopItemReceipt map(ShopItemReceiptDto shopItemReceipt);

    CompletedGameTask map(SolvedMessageDto solvedMessage);

    List<GameTask> mapMessages(List<MessageDto> messages);

    List<ShopItem> mapItems(List<ShopItemDto> shopItems);

    @Named("mapProbability")
    default TaskSuccessProbability mapProbability(String probability) {
        return TaskSuccessProbability.fromCode(probability);
    }

}
