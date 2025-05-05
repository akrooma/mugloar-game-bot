package com.example.mugloar.domain.game.bot.action;

import com.example.mugloar.common.constants.TaskSuccessProbability;
import com.example.mugloar.domain.game.GameService;
import com.example.mugloar.domain.game.bot.action.generic.Action;
import com.example.mugloar.domain.game.bot.action.generic.ActionResult;
import com.example.mugloar.domain.game.bot.action.result.CompleteTaskActionResult;
import com.example.mugloar.domain.game.state.GameState;
import com.example.mugloar.domain.game.task.CompletedGameTask;
import com.example.mugloar.domain.game.task.GameTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Tries to complete the easiest and best task.
 */
@Service
@RequiredArgsConstructor
public class CompleteEasiestTaskAction implements Action {

    private final GameService gameService;

    @Override
    public ActionResult execute(GameState gameState) {
        TaskSuccessProbability easiestTaskProbability = getEasiestTaskProbability(gameState.getUntouchedTasks())
                .orElseThrow();

        GameTask bestTask = getTheBestTask(gameState.getUntouchedTasks(), easiestTaskProbability)
                .orElseThrow();

        CompletedGameTask completedGameTask = gameService.tryCompleteTask(gameState.getGameId(), bestTask.taskId());

        return new CompleteTaskActionResult(bestTask.taskId(), completedGameTask);
    }

    protected static Optional<TaskSuccessProbability> getEasiestTaskProbability(List<GameTask> gameTasks) {
        return gameTasks.stream()
                .max(Comparator.comparingInt(gameTask -> gameTask.taskSuccessProbability().getValue()))
                .map(GameTask::taskSuccessProbability);
    }

    protected static Optional<GameTask> getTheBestTask(List<GameTask> gameTasks, TaskSuccessProbability easiestSuccessProbability) {
        return gameTasks.stream()
                .filter(gameTask -> gameTask.taskSuccessProbability().equals(easiestSuccessProbability))
                .max(Comparator.comparingInt(GameTask::reward));
    }

}
