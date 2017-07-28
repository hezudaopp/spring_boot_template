package com.youmayon.tutorial.exception;

/**
 * Created by Jawinton on 2017/7/28.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("user '" + userId + "' not found.");
    }
}
