package com.gestaodeloja.authenticator.exception;

public class UserJaExisteException extends RuntimeException {
    public UserJaExisteException() {
        super("O usuario informado ja existe.");
    }
}
