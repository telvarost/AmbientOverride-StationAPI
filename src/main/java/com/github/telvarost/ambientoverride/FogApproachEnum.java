package com.github.telvarost.ambientoverride;

public enum FogApproachEnum {
    LINEAR("Linear"),
    GROWTH("Growth");

    final String stringValue;

    FogApproachEnum() {
        this.stringValue = "Linear";
    }

    FogApproachEnum(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}