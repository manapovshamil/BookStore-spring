package com.example.MyBookShopApp.errs;

public class InvalidCredentialsException extends Exception{
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
