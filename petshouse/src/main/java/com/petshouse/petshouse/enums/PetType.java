package com.petshouse.petshouse.enums;

public enum PetType {
    DOG("Dog"),
    CAT("Cat"),
    PARROT("Parrot"),
    HAMSTER("Hamster"),
    RABBIT("Rabbit"),
    RAT("Rat"),
    TORTOISE("Tortoise"),
    FISH("Fish"),
    LIZARD("Lizard"),
    SNAKE("Snake");

    private final String displayName;

    PetType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
