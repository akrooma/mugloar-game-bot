package com.example.mugloar.common.constants;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskSuccessProbabilityUnitTests {

    @Test
    void getCode_should_return_expected_codes() {
        Map<TaskSuccessProbability, String> expectedTaskSuccessProbabilityCodes = getTaskSuccessProbabilitiesWithExpectedCodes();

        Set<TaskSuccessProbability> handledTaskSuccessProbabilities = expectedTaskSuccessProbabilityCodes.keySet();

        TaskSuccessProbability[] taskSuccessProbabilities = TaskSuccessProbability.values();

        for (TaskSuccessProbability taskSuccessProbability : taskSuccessProbabilities) {
            assertThatTaskSuccessProbabilityIsHandledByTheTest(taskSuccessProbability, handledTaskSuccessProbabilities);

            String expectedTaskSuccessProbabilityCode = expectedTaskSuccessProbabilityCodes.get(taskSuccessProbability);
            String actualTaskSuccessProbabilityCode = taskSuccessProbability.getCode();

            assertEquals(expectedTaskSuccessProbabilityCode, actualTaskSuccessProbabilityCode, String.format("TaskSuccessProbability %s seems to have a new code.", taskSuccessProbability));
        }
    }

    @Test
    void getValue_should_return_expected_values() {
        Map<TaskSuccessProbability, Integer> expectedTaskSuccessProbabilityValues = Map.ofEntries(
                Map.entry(TaskSuccessProbability.UNKNOWN, 1),
                Map.entry(TaskSuccessProbability.SURE_THING, 100),
                Map.entry(TaskSuccessProbability.PIECE_OF_CAKE,95),
                Map.entry(TaskSuccessProbability.WALK_IN_THE_PARK, 80),
                Map.entry(TaskSuccessProbability.QUITE_LIKELY, 70),
                Map.entry(TaskSuccessProbability.HMMM, 60),
                Map.entry(TaskSuccessProbability.GAMBLE, 50),
                Map.entry(TaskSuccessProbability.RISKY, 40),
                Map.entry(TaskSuccessProbability.RATHER_DETRIMENTAL, 20),
                Map.entry(TaskSuccessProbability.PLAYING_WITH_FIRE, 10),
                Map.entry(TaskSuccessProbability.SUICIDE_MISSION, 2)
        );

        Set<TaskSuccessProbability> handledTaskSuccessProbabilities = expectedTaskSuccessProbabilityValues.keySet();

        TaskSuccessProbability[] taskSuccessProbabilities = TaskSuccessProbability.values();

        for (TaskSuccessProbability taskSuccessProbability : taskSuccessProbabilities) {
            assertThatTaskSuccessProbabilityIsHandledByTheTest(taskSuccessProbability, handledTaskSuccessProbabilities);

            Integer expectedTaskSuccessProbabilityValue = expectedTaskSuccessProbabilityValues.get(taskSuccessProbability);
            int actualTaskSuccessProbabilityValue = taskSuccessProbability.getValue();

            assertEquals(expectedTaskSuccessProbabilityValue, actualTaskSuccessProbabilityValue, String.format("TaskSuccessProbability %s seems to have a new value.", taskSuccessProbability));
        }
    }

    @Test
    void fromCode_should_return_expected_enum_item() {
        Map<TaskSuccessProbability, String> expectedTaskSuccessProbabilities = getTaskSuccessProbabilitiesWithExpectedCodes();

        Set<TaskSuccessProbability> handledTaskSuccessProbabilities = expectedTaskSuccessProbabilities.keySet();

        for (TaskSuccessProbability taskSuccessProbability : TaskSuccessProbability.values()) {
            assertThatTaskSuccessProbabilityIsHandledByTheTest(taskSuccessProbability, handledTaskSuccessProbabilities);
        }

        expectedTaskSuccessProbabilities.forEach((expectedTaskSuccessProbability, taskSuccessProbabilityCode) -> {
            TaskSuccessProbability actualTaskSuccessProbability = TaskSuccessProbability.fromCode(taskSuccessProbabilityCode);
            String actualTaskSuccessProbabilityCode = actualTaskSuccessProbability.getCode();

            assertEquals(expectedTaskSuccessProbability, actualTaskSuccessProbability);
            assertEquals(taskSuccessProbabilityCode, actualTaskSuccessProbabilityCode);
        });
    }

    @Test
    void fromCode_should_default_to_UNKNOWN() {
        TaskSuccessProbability[] taskSuccessProbabilities = TaskSuccessProbability.values();

        assertTrue(taskSuccessProbabilities.length > 0, "TaskSuccessProbability should have enum items.");

        String[] codesThatDefaultToUnknown = {
                null,
                "",
                " ",
                taskSuccessProbabilities[0].getCode() + UUID.randomUUID()
        };

        TaskSuccessProbability expectedTaskSuccessProbability = TaskSuccessProbability.UNKNOWN;

        for (String codeThatDefaultsToUnknown : codesThatDefaultToUnknown) {
            TaskSuccessProbability actualTaskSuccessProbability = TaskSuccessProbability.fromCode(codeThatDefaultsToUnknown);

            assertEquals(expectedTaskSuccessProbability, actualTaskSuccessProbability);
        }
    }

    void assertThatTaskSuccessProbabilityIsHandledByTheTest(TaskSuccessProbability taskSuccessProbability, Set<TaskSuccessProbability> taskSuccessProbabilities) {
        boolean taskSuccessProbabilityIsHandledByTheTest = taskSuccessProbabilities.contains(taskSuccessProbability);

        assertTrue(taskSuccessProbabilityIsHandledByTheTest, String.format("TaskSuccessProbability %s is not handled by the test.", taskSuccessProbability));
    }

    Map<TaskSuccessProbability, String> getTaskSuccessProbabilitiesWithExpectedCodes() {
        return Map.ofEntries(
                Map.entry(TaskSuccessProbability.UNKNOWN, "unknown"),
                Map.entry(TaskSuccessProbability.SURE_THING, "Sure thing"),
                Map.entry(TaskSuccessProbability.PIECE_OF_CAKE,"Piece of cake"),
                Map.entry(TaskSuccessProbability.WALK_IN_THE_PARK, "Walk in the park"),
                Map.entry(TaskSuccessProbability.QUITE_LIKELY, "Quite likely"),
                Map.entry(TaskSuccessProbability.HMMM, "Hmmm...."),
                Map.entry(TaskSuccessProbability.GAMBLE, "Gamble"),
                Map.entry(TaskSuccessProbability.RISKY, "Risky"),
                Map.entry(TaskSuccessProbability.RATHER_DETRIMENTAL, "Rather detrimental"),
                Map.entry(TaskSuccessProbability.PLAYING_WITH_FIRE, "Playing with fire"),
                Map.entry(TaskSuccessProbability.SUICIDE_MISSION, "Suicide mission")
        );
    }

}
