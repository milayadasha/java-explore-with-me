package ru.practicum.ewm.server.service;

import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ViewStatsDto;

import java.util.List;

public interface StatsService {
    /**
     * Добавляет новый запрос к эндпоинту
     */
    EndpointHitDto addHit(EndpointHitDto endpointHit);

    /**
     * Возвращает список статистик по эндпоинтам
     */
    List<ViewStatsDto> getViewStats(String start, String end, List<String> uris, boolean unique);
}
