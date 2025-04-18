package com.petshouse.petshouse.enums;

public enum PetStatus {
    AVAILABLE("Available"),
    UNAVAILABLE("Unavailable");

    private final String displayName;

    PetStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
