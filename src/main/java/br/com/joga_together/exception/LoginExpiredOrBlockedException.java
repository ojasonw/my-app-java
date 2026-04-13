package br.com.joga_together.exception;

public class LoginExpiredOrBlockedException extends RuntimeException {
    public LoginExpiredOrBlockedException(String message){
        super(message);
    }
}
