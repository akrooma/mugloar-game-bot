package com.example.mugloar.common.constants;

import lombok.Getter;

/**
 * Well known and used item ID values.
 */
@Getter
public enum ItemId {

    /**
     * Health potion
     */
    HPOT("hpot");

    private final String value;

    ItemId(String value) {
        this.value = value;
    }

}
