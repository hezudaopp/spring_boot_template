package com.youmayon.tutorial.exception;

/**
 * @author Jawinton
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("user '" + userId + "' not found.");
    }
}
