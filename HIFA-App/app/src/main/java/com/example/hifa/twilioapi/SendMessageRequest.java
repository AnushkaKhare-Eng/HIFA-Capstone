package com.example.hifa.twilioapi;

public class SendMessageRequest {
    private String to_number;
    private String from_number = "+13236723036";
    private String message;

    public SendMessageRequest(String toNumber, String message) {
        this.to_number = toNumber;
        this.message = message;
    }

    public String getTo_number() {
        return to_number;
    }

    public void setTo_number(String to_number) {
        this.to_number = to_number;
    }

    public String getFrom_number() {
        return from_number;
    }

    public void setFrom_number(String from_number) {
        this.from_number = from_number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
