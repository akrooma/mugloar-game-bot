package com.example.mugloar.domain.game.bot.action;

import com.example.mugloar.domain.game.GameService;
import com.example.mugloar.domain.game.bot.action.generic.Action;
import com.example.mugloar.domain.game.bot.action.generic.ActionResult;
import com.example.mugloar.domain.game.bot.action.result.CompleteTaskActionResult;
import com.example.mugloar.domain.game.state.GameState;
import com.example.mugloar.domain.game.task.CompletedGameTask;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Tries to complete a specific task.
 */
@RequiredArgsConstructor
public class CompleteTaskAction implements Action {

    private final GameService gameService;

    @Getter
    private final String taskId;

    @Override
    public ActionResult execute(GameState gameState) {
        CompletedGameTask completedGameTask = gameService.tryCompleteTask(gameState.getGameId(), this.taskId);

        return new CompleteTaskActionResult(this.taskId, completedGameTask);
    }

}
