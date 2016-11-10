package io.academic.dao;

/**
 * Immutable message
 */
public class MessageDao {

    private String message;

    public MessageDao(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
