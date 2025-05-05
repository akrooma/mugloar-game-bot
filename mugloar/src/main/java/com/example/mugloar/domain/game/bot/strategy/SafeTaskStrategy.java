package com.example.mugloar.domain.game.bot.strategy;

import com.example.mugloar.common.constants.TaskSuccessProbability;
import com.example.mugloar.domain.game.GameService;
import com.example.mugloar.domain.game.bot.action.CompleteTaskAction;
import com.example.mugloar.domain.game.bot.action.generic.Action;
import com.example.mugloar.domain.game.bot.strategy.generic.Strategy;
import com.example.mugloar.domain.game.state.GameState;
import com.example.mugloar.domain.game.task.GameTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * If a safe task exists, try to complete the one with the highest reward.
 */
@Service
@RequiredArgsConstructor
public class SafeTaskStrategy implements Strategy {

    private static final TaskSuccessProbability SAFE_TASK_PROBABILITY = TaskSuccessProbability.SURE_THING;

    private final GameService gameService;

    @Override
    public Optional<Action> evaluateGameState(GameState gameState) {
        return findBestSafeTask(gameState.getUntouchedTasks())
                .map(gameTask -> new CompleteTaskAction(gameService, gameTask.taskId()));
    }

    protected static Optional<GameTask> findBestSafeTask(List<GameTask> gameTasks) {
        return gameTasks.stream()
                .filter(gameTask -> gameTask.taskSuccessProbability().equals(SAFE_TASK_PROBABILITY))
                .max(Comparator.comparingInt(GameTask::reward));
    }

}
