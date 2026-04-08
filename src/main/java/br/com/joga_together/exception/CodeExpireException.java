package br.com.joga_together.exception;

public class CodeExpireException extends RuntimeException {
    public CodeExpireException(String message) {
        super(message);
    }
}
