package com.petshouse.petshouse.enums;

public enum PetType {
    DOG("Dog"),
    CAT("Cat"),
    CROW("Crow"),
    HORSE("Horse"),
    FROG("Frog"),
    SPIDER("Spider"),
    DOVE("Dove"),
    FISH("Fish"),
    DRAGON("Dragon");

    private final String displayName;

    PetType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
