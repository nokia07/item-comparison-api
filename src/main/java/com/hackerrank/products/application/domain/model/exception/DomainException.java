package com.hackerrank.products.application.domain.model.exception;

public abstract class DomainException extends RuntimeException {
    private final String title;

    protected DomainException(String title, String message) {
        super(message);
        this.title = title;
    }
    
    public String getTitle() {
        return title;
    }

}
