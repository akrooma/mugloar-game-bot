package com.example.mugloar.domain.game.bot.action.result;

import com.example.mugloar.domain.game.bot.action.generic.ActionResult;
import com.example.mugloar.domain.game.state.GameState;
import com.example.mugloar.domain.game.task.CompletedGameTask;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CompleteTaskActionResult implements ActionResult {

    @Getter
    private final String taskId;

    @Getter
    private final CompletedGameTask completedGameTask;

    @Override
    public void updateGameState(GameState gameState) {
        gameState.updateTaskCompletion(this.taskId, this.completedGameTask);
    }

}
