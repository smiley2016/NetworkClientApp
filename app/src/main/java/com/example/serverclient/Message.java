package com.example.serverclient;

public class Message {
    private String message;
    private boolean fromMe;

    Message(String message, boolean fromMe) {
        this.message = message;
        this.fromMe = fromMe;
    }

    String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    boolean isFromMe() {
        return fromMe;
    }

    public void setFromMe(boolean fromMe) {
        this.fromMe = fromMe;
    }
}
