package com.example.mugloar.domain.game.state;

import com.example.mugloar.domain.game.shop.ShopItem;
import com.example.mugloar.domain.game.shop.ShopItemReceipt;
import com.example.mugloar.domain.game.task.CompletedGameTask;
import com.example.mugloar.domain.game.task.GameTask;
import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@ToString
public class GameState {

    private final static int DEAD_LIVES_COUNT = 0;
    private final static int CRITICAL_LIVES_COUNT = 1;

    private final String gameId;
    private boolean isGameOver;
    private int lives;
    private int gold;
    private int score;
    private int level;
    private int turn;
    private List<GameTask> gameTasks;
    private List<ShopItem> shopItems;
    private final Set<String> failedTasks;
    private final Set<String> inventory;

    public GameState(
            NewGame newGame,
            List<GameTask> gameTasks,
            List<ShopItem> shopItems
    ) {
        this.gameId = newGame.gameId();
        this.lives = newGame.lives();
        this.gold = newGame.gold();
        this.score = newGame.score();
        this.level = newGame.level();
        this.turn = newGame.turn();
        this.gameTasks = gameTasks;
        this.shopItems = shopItems;
        this.failedTasks = new HashSet<>();
        this.inventory = new HashSet<>();
    }

    public void setGameOver() {
        this.isGameOver = true;
    }

    public void updateForHealthPotionPurchase(ShopItemReceipt healthPotionReceipt) {
        this.lives = healthPotionReceipt.lives();
        this.gold = healthPotionReceipt.gold();
        this.level = healthPotionReceipt.level();
        this.turn = healthPotionReceipt.turn();
    }

    public void updateTaskCompletion(String taskId, CompletedGameTask completedGameTask) {
        if (completedGameTask.success()) {
            updateSuccessfulTask(taskId, completedGameTask);
        } else {
            updateFailedTask(taskId, completedGameTask);
        }
    }

    public void updateForUpgradePurchase(String itemId, ShopItemReceipt shopItemReceipt) {
        this.inventory.add(itemId);
        updateForHealthPotionPurchase(shopItemReceipt);
    }

    public void updateTasks(List<GameTask> gameTasks) {
        this.gameTasks = gameTasks;
    }

    public void updateShopItems(List<ShopItem> shopItems) {
        this.shopItems = shopItems;
    }

    private void updateSuccessfulTask(String taskId, CompletedGameTask completedGameTask) {
        this.failedTasks.remove(taskId);
        updatePropertiesFromTask(completedGameTask);
    }

    private void updateFailedTask(String taskId, CompletedGameTask completedGameTask) {
        this.failedTasks.add(taskId);
        updatePropertiesFromTask(completedGameTask);
    }

    private void updatePropertiesFromTask(CompletedGameTask completedGameTask) {
        this.lives = completedGameTask.lives();
        this.gold = completedGameTask.gold();
        this.score = completedGameTask.score();
        this.turn = completedGameTask.turn();
    }

    public String getGameEndMessage() {
        return this.isGameOver
                ? "Game is over!"
                : "Game reached a specific score amount.";
    }

    /**
     * @return list of tasks which have not been failed
     */
    public List<GameTask> getUntouchedTasks() {
        if (this.failedTasks.isEmpty()) return this.gameTasks;

        return this.gameTasks.stream()
                .filter(gameTask -> !this.failedTasks.contains(gameTask.taskId()))
                .toList();
    }

    public boolean hasItem(String itemId) {
        return this.inventory.contains(itemId);
    }

    public boolean playerIsAlive() {
        return livesExceedAmount(DEAD_LIVES_COUNT);
    }

    public boolean playerIsHealthy() {
        return livesExceedAmount(CRITICAL_LIVES_COUNT);
    }

    private boolean livesExceedAmount(int amount) {
        return this.lives > amount;
    }

}
