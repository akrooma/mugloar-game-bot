package com.example.mugloar.domain.game.bot;

import com.example.mugloar.domain.game.GameService;
import com.example.mugloar.domain.game.bot.action.generic.Action;
import com.example.mugloar.domain.game.bot.action.generic.ActionResult;
import com.example.mugloar.domain.game.bot.decision.DecisionEngine;
import com.example.mugloar.domain.game.shop.ShopItem;
import com.example.mugloar.domain.game.state.GameState;
import com.example.mugloar.domain.game.task.GameTask;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

/**
 * Attempts to play the Dragons of Mugloar game by getting the highest possible score without dying.
 */
@Component
@RequiredArgsConstructor
public class GameBot {

    @Value("${game.score-breakpoint:50000}")
    private int scoreBreakpoint;

    private final GameService gameService;
    private final DecisionEngine decisionEngine;

    /**
     * Starts a new game, tries to reach the highest score by taking different actions defined by strategies.
     * @return final game state once the bot stopped playing
     */
    public GameState playGame() {
        GameState gameState = gameService.startNewGame();

        while (keepPlaying(gameState)) {
            Action action = decisionEngine.decideNextAction(gameState);

            try {
                ActionResult actionResult = action.execute(gameState);

                updateGameState(gameState, actionResult);
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.GONE) {
                    gameState.setGameOver();
                }
            }
        }

        return gameState;
    }

    /**
     * Keep playing if the player is still alive and the score has not broken the specified threshold.
     * @param gameState current game state
     * @return true if the bot should keep on playing.
     */
    protected boolean keepPlaying(GameState gameState) {
        return gameState.playerIsAlive()
                && !gameState.isGameOver()
                && gameState.getScore() < scoreBreakpoint;
    }

    /**
     * Updates the given game state with new tasks, last action result and refreshes shop items.
     * @param gameState game state during last action
     * @param actionResult last action's result
     */
    private void updateGameState(GameState gameState, ActionResult actionResult) {
        List<GameTask> availableGameTasks = gameService.getAvailableTasks(gameState.getGameId());
        List<ShopItem> availableShopItems = gameService.getAvailableShopItems(gameState.getGameId());

        gameState.updateShopItems(availableShopItems);
        gameState.updateTasks(availableGameTasks);

        actionResult.updateGameState(gameState);
    }

}
