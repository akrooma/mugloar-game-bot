package com.example.mugloar.common.constants;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemIdUnitTests {

    @Test
    void getValue_should_return_expected_values() {
        Map<ItemId, String> expectedItemIdValues = Map.of(
                ItemId.HPOT, "hpot"
            );

        ItemId[] itemIds = ItemId.values();

        for (ItemId itemId : itemIds) {
            boolean itemIdIsHandledByTheTest = expectedItemIdValues.containsKey(itemId);

            assertTrue(itemIdIsHandledByTheTest, String.format("ItemId %s is not handled by the test.", itemId));

            String expectedItemIdValue = expectedItemIdValues.get(itemId);
            String actualItemIdValue = itemId.getValue();

            assertEquals(expectedItemIdValue, actualItemIdValue, String.format("ItemId %s seems to have a new value.", itemId));
        }
    }

}
