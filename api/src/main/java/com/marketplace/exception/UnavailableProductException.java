package com.marketplace.exception;

public class UnavailableProductException extends RuntimeException {
    public UnavailableProductException(String message) {
        super(message);
    }
}
