package ru.practicum.ewm.server.model;

import lombok.Getter;

@Getter
public class ErrorResponse {
    String error;
    String description;

    public ErrorResponse(String error,String description) {
        this.description = description;
        this.error = error;
    }
}
