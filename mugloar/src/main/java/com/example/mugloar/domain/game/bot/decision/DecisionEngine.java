package com.example.mugloar.domain.game.bot.decision;

import com.example.mugloar.domain.game.bot.action.CompleteEasiestTaskAction;
import com.example.mugloar.domain.game.bot.action.generic.Action;
import com.example.mugloar.domain.game.bot.strategy.generic.Strategy;
import com.example.mugloar.domain.game.bot.strategy.generic.StrategyConstants;
import com.example.mugloar.domain.game.state.GameState;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DecisionEngine {

    private final List<Strategy> orderedStrategies;

    private final CompleteEasiestTaskAction defaultAction;

    public DecisionEngine(List<Strategy> strategies, CompleteEasiestTaskAction completeEasiestTaskAction) {
        orderedStrategies = getOrderedStrategies(strategies);
        defaultAction = completeEasiestTaskAction;
    }

    public Action decideNextAction(GameState gameState) {
        for (Strategy strategy : orderedStrategies) {
            Optional<Action> actionOpt = strategy.evaluateGameState(gameState);

            if (actionOpt.isPresent()) return actionOpt.get();
        }

        return defaultAction;
    }

    protected List<Strategy> getOrderedStrategies(List<Strategy> strategies) {
        List<Strategy> orderedStrategies = new ArrayList<>();

        var strategyOrderItems = StrategyConstants.STRATEGY_ORDER;

        for (var strategyOrderItem : strategyOrderItems) {
            Optional<Strategy> strategyOpt = strategies.stream()
                    .filter(strat -> strat.getClass().equals(strategyOrderItem))
                    .findFirst();

            if (strategyOpt.isEmpty()) continue;

            orderedStrategies.add(strategyOpt.get());
        }

        return orderedStrategies;
    }

}
