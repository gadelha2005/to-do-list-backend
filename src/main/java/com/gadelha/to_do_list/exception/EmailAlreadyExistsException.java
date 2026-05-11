package com.gadelha.to_do_list.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("E-mail já cadastrado: " + email);
    }
}
