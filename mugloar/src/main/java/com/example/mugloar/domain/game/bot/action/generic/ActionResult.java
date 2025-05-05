package com.example.mugloar.domain.game.bot.action.generic;

import com.example.mugloar.domain.game.state.GameState;

public interface ActionResult {

    void updateGameState(GameState gameState);

}
