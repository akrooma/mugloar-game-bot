package com.example.mugloar.domain.game.bot.strategy;

import com.example.mugloar.common.constants.TaskSuccessProbability;
import com.example.mugloar.domain.game.GameService;
import com.example.mugloar.domain.game.bot.action.CompleteTaskAction;
import com.example.mugloar.domain.game.bot.action.generic.Action;
import com.example.mugloar.domain.game.state.GameState;
import com.example.mugloar.domain.game.state.NewGame;
import com.example.mugloar.domain.game.task.CompletedGameTask;
import com.example.mugloar.domain.game.task.GameTask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class SafeTaskStrategyUnitTests {

    private static final TaskSuccessProbability SAFE_TASK_PROBABILITY = TaskSuccessProbability.SURE_THING;

    private final GameService gameService = mock(GameService.class);

    private final SafeTaskStrategy safeTaskStrategy = new SafeTaskStrategy(gameService);

    @Test
    void findBestSafeTask_should_find_safe_task_with_highest_reward() {
        GameTask expectedTask = GameTask.builder()
                .taskId("easiest-best-task")
                .reward(10)
                .taskSuccessProbability(SAFE_TASK_PROBABILITY)
                .build();

        List<GameTask> gameTasks = List.of(
                GameTask.builder()
                        .taskId("easiest-worse-task")
                        .reward(expectedTask.reward() - 1)
                        .taskSuccessProbability(expectedTask.taskSuccessProbability())
                        .build(),
                GameTask.builder()
                        .taskId("hardest-task")
                        .reward(69)
                        .taskSuccessProbability(TaskSuccessProbability.RISKY)
                        .build(),
                expectedTask
        );

        Optional<GameTask> actualTaskOpt = SafeTaskStrategy.findBestSafeTask(gameTasks);

        assertTrue(actualTaskOpt.isPresent());

        GameTask actualTask = actualTaskOpt.get();

        assertEquals(expectedTask.taskId(), actualTask.taskId());
        assertEquals(expectedTask.reward(), actualTask.reward());
    }

    @Test
    void findBestSafeTask_should_not_return_task_when_safe_one_is_not_present() {
        List<GameTask> gameTasks = List.of(
                GameTask.builder()
                        .taskId("gamble-task")
                        .reward(1)
                        .taskSuccessProbability(TaskSuccessProbability.GAMBLE)
                        .build(),
                GameTask.builder()
                        .taskId("risky-task")
                        .reward(2)
                        .taskSuccessProbability(TaskSuccessProbability.RISKY)
                        .build()
        );

        Optional<GameTask> gameTaskOpt = SafeTaskStrategy.findBestSafeTask(gameTasks);

        assertTrue(gameTaskOpt.isEmpty());
    }

    @Test
    void evaluateGameState_should_not_return_action_if_there_are_no_safe_tasks() {
        NewGame newGame = NewGame.builder()
                .gameId("some-game-id")
                .lives(3)
                .gold(0)
                .score(0)
                .turn(0)
                .build();

        List<GameTask> gameTasks = List.of(
                GameTask.builder()
                        .taskId("gamble-task")
                        .reward(1)
                        .taskSuccessProbability(TaskSuccessProbability.GAMBLE)
                        .build(),
                GameTask.builder()
                        .taskId("risky-task")
                        .reward(2)
                        .taskSuccessProbability(TaskSuccessProbability.RISKY)
                        .build()
        );

        GameState gameState = new GameState(newGame, gameTasks, List.of());

        Optional<Action> actionOpt = safeTaskStrategy.evaluateGameState(gameState);

        assertTrue(actionOpt.isEmpty());
    }

    @Test
    void evaluateGameState_should_not_return_action_if_safe_task_is_already_failed() {
        NewGame newGame = NewGame.builder()
                .gameId("some-game-id")
                .lives(3)
                .gold(0)
                .score(0)
                .turn(0)
                .build();

        GameTask failedSafeTask = GameTask.builder()
                .taskId("easiest-best-task")
                .reward(10)
                .taskSuccessProbability(SAFE_TASK_PROBABILITY)
                .build();

        List<GameTask> gameTasks = List.of(
                GameTask.builder()
                        .taskId("gamble-task")
                        .reward(1)
                        .taskSuccessProbability(TaskSuccessProbability.GAMBLE)
                        .build(),
                failedSafeTask
        );

        GameState gameState = new GameState(newGame, gameTasks, List.of());

        gameState.updateTaskCompletion(failedSafeTask.taskId(), CompletedGameTask.builder().success(false).build());

        Optional<Action> actionOpt = safeTaskStrategy.evaluateGameState(gameState);

        assertTrue(actionOpt.isEmpty());
    }

    @Test
    void evaluateGameState_should_return_action_if_untouched_safe_task_is_available() {
        NewGame newGame = NewGame.builder()
                .gameId("some-game-id")
                .lives(3)
                .gold(0)
                .score(0)
                .turn(0)
                .build();

        GameTask expectedTask = GameTask.builder()
                .taskId("easiest-best-task")
                .reward(10)
                .taskSuccessProbability(SAFE_TASK_PROBABILITY)
                .build();

        List<GameTask> gameTasks = List.of(
                GameTask.builder()
                        .taskId("gamble-task")
                        .reward(1)
                        .taskSuccessProbability(TaskSuccessProbability.GAMBLE)
                        .build(),
                expectedTask
        );

        GameState gameState = new GameState(newGame, gameTasks, List.of());

        Optional<Action> actionOpt = safeTaskStrategy.evaluateGameState(gameState);

        assertTrue(actionOpt.isPresent());

        Action action = actionOpt.get();

        assertInstanceOf(CompleteTaskAction.class, action);

        CompleteTaskAction completeTaskAction = (CompleteTaskAction) action;

        assertEquals(expectedTask.taskId(), completeTaskAction.getTaskId());
    }

}
