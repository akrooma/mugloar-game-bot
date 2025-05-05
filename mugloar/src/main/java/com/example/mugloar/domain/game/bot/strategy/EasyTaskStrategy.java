package com.example.mugloar.domain.game.bot.strategy;

import com.example.mugloar.common.constants.TaskSuccessProbability;
import com.example.mugloar.domain.game.GameService;
import com.example.mugloar.domain.game.bot.action.CompleteTaskAction;
import com.example.mugloar.domain.game.bot.action.generic.Action;
import com.example.mugloar.domain.game.bot.strategy.generic.Strategy;
import com.example.mugloar.domain.game.bot.strategy.generic.StrategyHelpers;
import com.example.mugloar.domain.game.state.GameState;
import com.example.mugloar.domain.game.task.GameTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Complete an easy task with the highest reward and success probability ratio.
 */
@Service
@RequiredArgsConstructor
public class EasyTaskStrategy implements Strategy {

    private static final List<TaskSuccessProbability> EASY_TASK_PROBABILITIES = List.of(
            TaskSuccessProbability.PIECE_OF_CAKE,
            TaskSuccessProbability.WALK_IN_THE_PARK
    );

    private final GameService gameService;

    @Override
    public Optional<Action> evaluateGameState(GameState gameState) {
        return findBestEasyTask(gameState.getUntouchedTasks())
                .map(gameTask -> new CompleteTaskAction(gameService, gameTask.taskId()));
    }

    protected Optional<GameTask> findBestEasyTask(List<GameTask> gameTasks) {
        return gameTasks.stream()
                .filter(gameTask -> EASY_TASK_PROBABILITIES.contains(gameTask.taskSuccessProbability()))
                .max(Comparator.comparingInt(StrategyHelpers::calculateTaskScore));
    }

}
