package com.articlefetch.app.Busniess.Exceptions;

public class DuplicateEntryException extends RuntimeException {

    public DuplicateEntryException() {
        super("Certain fields already Exists");
    }

    public DuplicateEntryException(String message) {
        super(message);
    }
}
