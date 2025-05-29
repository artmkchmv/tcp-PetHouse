package com.petshouse.petshouse.exceptions;

public class UnauthorizedException extends APIException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
