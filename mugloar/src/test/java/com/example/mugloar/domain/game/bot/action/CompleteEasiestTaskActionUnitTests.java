package com.example.mugloar.domain.game.bot.action;

import com.example.mugloar.common.constants.TaskSuccessProbability;
import com.example.mugloar.domain.game.GameService;
import com.example.mugloar.domain.game.bot.action.generic.ActionResult;
import com.example.mugloar.domain.game.bot.action.result.CompleteTaskActionResult;
import com.example.mugloar.domain.game.state.GameState;
import com.example.mugloar.domain.game.state.NewGame;
import com.example.mugloar.domain.game.task.CompletedGameTask;
import com.example.mugloar.domain.game.task.GameTask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompleteEasiestTaskActionUnitTests {

    private final GameService gameService = mock(GameService.class);

    private final CompleteEasiestTaskAction completeEasiestTaskAction = new CompleteEasiestTaskAction(gameService);

    @Test
    void getEasiestTaskProbability_should_return_easiest_probability() {
        List<TaskSuccessProbability> probabilities = List.of(TaskSuccessProbability.values());

        List<GameTask> gameTasks = new ArrayList<>();

        for (int i = 0; i < probabilities.size(); i++) {
            GameTask gameTask = GameTask.builder()
                    .taskId(Integer.toString(i))
                    .taskSuccessProbability(probabilities.get(i))
                    .build();

            gameTasks.add(i, gameTask);
        }

        TaskSuccessProbability expectedEasiestProbability = probabilities.stream()
                .max(Comparator.comparingInt(TaskSuccessProbability::getValue))
                .orElseThrow();

        TaskSuccessProbability actualEasiestProbability = CompleteEasiestTaskAction.getEasiestTaskProbability(gameTasks)
                .orElseThrow();

        assertEquals(expectedEasiestProbability, actualEasiestProbability);
    }

    @Test
    void getTheBestTask_should_return_best_task() {
        GameTask expectedTask = GameTask.builder()
                .taskId("easiest-best-task")
                .reward(10)
                .taskSuccessProbability(TaskSuccessProbability.SURE_THING)
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

        GameTask bestTask = CompleteEasiestTaskAction.getTheBestTask(gameTasks, expectedTask.taskSuccessProbability())
                .orElseThrow();

        assertEquals(expectedTask.taskId(), bestTask.taskId());
    }

    @Test
    void execute_should_return_expected_action_result() {
        GameTask expectedTask = GameTask.builder()
                .taskId("easiest-best-task")
                .reward(10)
                .taskSuccessProbability(TaskSuccessProbability.SURE_THING)
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

        NewGame newGame = NewGame.builder()
                .gameId("some-game-id")
                .lives(3)
                .gold(0)
                .score(0)
                .turn(0)
                .build();

        GameState gameState = new GameState(newGame, gameTasks, List.of());

        CompletedGameTask completedGameTask = CompletedGameTask.builder()
                .success(true)
                .gold(expectedTask.reward())
                .build();

        when(gameService.tryCompleteTask(any(), any()))
                .thenReturn(completedGameTask);

        ActionResult actionResult = completeEasiestTaskAction.execute(gameState);

        assertNotNull(actionResult);
        assertInstanceOf(CompleteTaskActionResult.class, actionResult);

        CompleteTaskActionResult completeTaskActionResult = (CompleteTaskActionResult) actionResult;

        assertEquals(expectedTask.taskId(), completeTaskActionResult.getTaskId());
        assertEquals(expectedTask.reward(), completeTaskActionResult.getCompletedGameTask().gold());
    }

}
