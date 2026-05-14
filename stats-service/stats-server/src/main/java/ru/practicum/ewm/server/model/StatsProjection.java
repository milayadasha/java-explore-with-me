package ru.practicum.ewm.server.model;

public interface StatsProjection {
    String getApp();

    String getUri();

    Long getHits();
}