package com.example.mugloar.domain.game.bot.strategy.generic;

import com.example.mugloar.domain.game.bot.action.generic.Action;
import com.example.mugloar.domain.game.state.GameState;

import java.util.Optional;

public interface Strategy {

    Optional<Action> evaluateGameState(GameState gameState);

}
