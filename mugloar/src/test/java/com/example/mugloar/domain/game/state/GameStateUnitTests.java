package com.example.mugloar.domain.game.state;

import com.example.mugloar.common.constants.TaskSuccessProbability;
import com.example.mugloar.domain.game.shop.ShopItem;
import com.example.mugloar.domain.game.task.CompletedGameTask;
import com.example.mugloar.domain.game.task.GameTask;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

public class GameStateUnitTests {

    private final static int DEAD_LIVES_COUNT = 0;
    private final static int CRITICAL_LIVES_COUNT = 1;

    @Test
    void constructor_should_initialize_required_fields() {
        NewGame newGame = getNewGame();

        List<GameTask> inputGameTasks = List.of(
                GameTask.builder()
                        .taskId("some-task-id")
                        .taskSuccessProbability(TaskSuccessProbability.SURE_THING)
                        .build()
        );

        List<ShopItem> inputShopItems = List.of(
                ShopItem.builder()
                        .id("some-item-id")
                        .build()
        );

        GameState gameState = new GameState(newGame, inputGameTasks, inputShopItems);

        assertEquals(newGame.gameId(), gameState.getGameId());
        assertEquals(newGame.lives(), gameState.getLives());
        assertEquals(newGame.gold(), gameState.getGold());
        assertEquals(newGame.score(), gameState.getScore());
        assertEquals(newGame.level(), gameState.getLevel());
        assertEquals(newGame.turn(), gameState.getTurn());

        Set<String> failedTasks = gameState.getFailedTasks();
        assertNotNull(failedTasks);
        assertEquals(0, failedTasks.size());

        Set<String> inventory = gameState.getInventory();
        assertNotNull(inventory);
        assertEquals(0, inventory.size());

        assertThat(gameState.getGameTasks(), is(inputGameTasks));
        assertThat(gameState.getShopItems(), is(inputShopItems));
    }

    @Test
    void updateTaskCompletion_should_update_game_state_fields_for_successful_task() {
        NewGame newGame = getNewGame();

        GameState gameState = new GameState(newGame, List.of(), List.of());

        String successfulTaskId = "successful-task-id";
        CompletedGameTask successfulTask = CompletedGameTask.builder()
                .success(true)
                .lives(2)
                .gold(1)
                .score(2)
                .turn(3)
                .build();

        gameState.updateTaskCompletion(successfulTaskId, successfulTask);

        assertFieldsMatchAfterTaskCompletionUpdate(successfulTask, gameState);

        boolean failedTasksDoesNotContainTaskId = !gameState.getFailedTasks().contains(successfulTaskId);
        assertTrue(failedTasksDoesNotContainTaskId);
    }

    @Test
    void updateTaskCompletion_should_update_game_state_fields_for_failed_task() {
        NewGame newGame = getNewGame();

        GameState gameState = new GameState(newGame, List.of(), List.of());

        String failedTaskId = "failed-task-id";
        CompletedGameTask failedTask = CompletedGameTask.builder()
                .success(false)
                .lives(1)
                .gold(2)
                .score(3)
                .turn(4)
                .build();

        gameState.updateTaskCompletion(failedTaskId, failedTask);

        assertFieldsMatchAfterTaskCompletionUpdate(failedTask, gameState);

        boolean failedTasksContainsTaskId = gameState.getFailedTasks().contains(failedTaskId);
        assertTrue(failedTasksContainsTaskId);
    }

    @Test
    void updateTaskCompletion_should_remove_failed_task_for_successful_completion() {
        NewGame newGame = getNewGame();

        GameTask failedGameTask = GameTask.builder()
                .taskId("failed-task-id")
                .taskSuccessProbability(TaskSuccessProbability.SUICIDE_MISSION)
                .build();

        GameState gameState = new GameState(newGame, List.of(failedGameTask), List.of());

        String taskId = failedGameTask.taskId();

        gameState.updateTaskCompletion(taskId, CompletedGameTask.builder().success(false).build());

        boolean failedTasksContainsTaskId = gameState.getFailedTasks().contains(taskId);
        assertTrue(failedTasksContainsTaskId);

        gameState.updateTaskCompletion(taskId, CompletedGameTask.builder().success(true).build());

        boolean failedTasksDoesNotContainTaskId = !gameState.getFailedTasks().contains(taskId);
        assertTrue(failedTasksDoesNotContainTaskId);
    }

    @Test
    void getUntouchedTasks_should_filter_out_failed_tasks() {
        NewGame newGame = getNewGame();

        GameTask succesfulGameTask = GameTask.builder()
                .taskId("successful-task-id")
                .taskSuccessProbability(TaskSuccessProbability.SURE_THING)
                .build();

        GameTask failedGameTask = GameTask.builder()
                .taskId("failed-task-id")
                .taskSuccessProbability(TaskSuccessProbability.SUICIDE_MISSION)
                .build();

        GameState gameState = new GameState(newGame, List.of(succesfulGameTask, failedGameTask), List.of());

        gameState.updateTaskCompletion(succesfulGameTask.taskId(), CompletedGameTask.builder().success(true).build());
        gameState.updateTaskCompletion(failedGameTask.taskId(), CompletedGameTask.builder().success(false).build());

        List<GameTask> untouchedTasks = gameState.getUntouchedTasks();

        assertThat(untouchedTasks, is(List.of(succesfulGameTask)));
    }

    @Test
    void playerIsAlive_should_return_true_with_sufficient_health() {
        NewGame newGame = NewGame.builder()
                .gameId("some-game-id")
                .lives(DEAD_LIVES_COUNT + 1)
                .gold(0)
                .score(0)
                .turn(0)
                .build();

        GameState gameState = new GameState(newGame, List.of(), List.of());

        assertTrue(gameState.playerIsAlive());
    }

    @Test
    void playerIsAlive_should_return_false_with_insufficient_health() {
        NewGame newGame = NewGame.builder()
                .gameId("some-game-id")
                .lives(DEAD_LIVES_COUNT)
                .gold(0)
                .score(0)
                .turn(0)
                .build();

        GameState gameState = new GameState(newGame, List.of(), List.of());

        assertFalse(gameState.playerIsAlive());
    }

    @Test
    void playerIsHealthy_should_return_true_with_sufficient_health() {
        NewGame newGame = NewGame.builder()
                .gameId("some-game-id")
                .lives(CRITICAL_LIVES_COUNT + 1)
                .gold(0)
                .score(0)
                .turn(0)
                .build();

        GameState gameState = new GameState(newGame, List.of(), List.of());

        assertTrue(gameState.playerIsHealthy());
    }

    @Test
    void playerIsHealthy_should_return_false_with_insufficient_health() {
        NewGame newGame = NewGame.builder()
                .gameId("some-game-id")
                .lives(CRITICAL_LIVES_COUNT)
                .gold(0)
                .score(0)
                .turn(0)
                .build();

        GameState gameState = new GameState(newGame, List.of(), List.of());

        assertFalse(gameState.playerIsHealthy());
    }

    void assertFieldsMatchAfterTaskCompletionUpdate(CompletedGameTask completedGameTask, GameState gameState) {
        assertEquals(completedGameTask.lives(), gameState.getLives());
        assertEquals(completedGameTask.gold(), gameState.getGold());
        assertEquals(completedGameTask.score(), gameState.getScore());
        assertEquals(completedGameTask.turn(), gameState.getTurn());
    }

    static NewGame getNewGame() {
        return NewGame.builder()
                .gameId("some-game-id")
                .lives(3)
                .gold(0)
                .score(0)
                .turn(0)
                .build();
    }

}
