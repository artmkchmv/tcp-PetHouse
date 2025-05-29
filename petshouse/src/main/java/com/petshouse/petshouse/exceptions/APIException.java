package com.petshouse.petshouse.exceptions;

public abstract class APIException extends RuntimeException {
    public APIException(String message) {
        super(message);
    }
}
