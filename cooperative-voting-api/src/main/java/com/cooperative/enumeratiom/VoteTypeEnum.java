package com.cooperative.enumeratiom;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum VoteTypeEnum {

    YES,
    NO;

    @JsonCreator
    public static VoteTypeEnum fromString(String value) {
        if (value == null) {
            return null;
        }

        try {
            return VoteTypeEnum.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    value + " is not a valid vote type. Use YES or NO."
            );
        }
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
