package com.telemed24.exception;

@SuppressWarnings("serial")
public class UserWithEmailAlreadyExistException extends RuntimeException {
    public UserWithEmailAlreadyExistException() {
        super();
        System.out.println("\nuser already exist");
    }
}
