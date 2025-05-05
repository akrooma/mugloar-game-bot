package com.example.mugloar.common.constants;

import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * Task success probabilities.
 * Code is text representation.
 * Value is numeric representation - higher the value, better the success chance.
 */
@Getter
public enum TaskSuccessProbability {

    UNKNOWN("unknown", 1),
    SURE_THING("Sure thing", 100),
    PIECE_OF_CAKE("Piece of cake", 95),
    WALK_IN_THE_PARK("Walk in the park", 80),
    QUITE_LIKELY("Quite likely", 70),
    HMMM("Hmmm....", 60),
    GAMBLE("Gamble", 50),
    RISKY("Risky", 40),
    RATHER_DETRIMENTAL("Rather detrimental", 20),
    PLAYING_WITH_FIRE("Playing with fire", 10),
    SUICIDE_MISSION("Suicide mission", 2);

    private final String code;
    private final int value;

    TaskSuccessProbability(String code, int value) {
        this.code = code;
        this.value = value;
    }

    /**
     * Tries to find an enum item with the corresponding code.
     * Defaults to {@link TaskSuccessProbability#UNKNOWN} if no suitable enum item is found.
     * @param code task success probability code
     * @return corresponding enum item or {@link TaskSuccessProbability#UNKNOWN}.
     */
    public static TaskSuccessProbability fromCode(String code) {
        if (!StringUtils.hasText(code)) return TaskSuccessProbability.UNKNOWN;

        return Arrays.stream(TaskSuccessProbability.values())
                .filter(taskSuccessProbability -> taskSuccessProbability.getCode().equals(code))
                .findFirst()
                .orElse(TaskSuccessProbability.UNKNOWN);
    }

}
