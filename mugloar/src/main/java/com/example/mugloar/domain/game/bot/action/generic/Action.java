package com.example.mugloar.domain.game.bot.action.generic;

import com.example.mugloar.domain.game.state.GameState;

public interface Action {

    ActionResult execute(GameState gameState);

}
