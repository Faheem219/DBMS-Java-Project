package com.stockapp.util;

/**
 * Custom checked exception for DAO/service layer.
 */
public class AppException extends Exception {
    public AppException(String message) {
        super(message);
    }
    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}
