package com.petshouse.petshouse.exceptions;

public class BadRequestException extends APIException {
    public BadRequestException(String message) {
        super(message);
    }
}
