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
        return VoteTypeEnum.valueOf(value.trim().toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
