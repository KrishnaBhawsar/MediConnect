package com.telemed24.exception;

@SuppressWarnings("serial")
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super(email);
        System.out.println("\nUser not found exception");
    }
}
