package ru.practicum.ewm.server.mapper;

import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.server.model.EndpointHit;
import ru.practicum.ewm.server.model.StatsProjection;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatsMapper {
    public static EndpointHitDto toEndpointHitDto(EndpointHit endpointHit,
                                                  DateTimeFormatter dateTimeFormatter) {
        return EndpointHitDto.builder()
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(dateTimeFormatter.format(endpointHit.getTimestamp()))
                .build();
    }

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto,
                                            DateTimeFormatter dateTimeFormatter) {
        return EndpointHit.builder()
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(LocalDateTime.parse(endpointHitDto.getTimestamp(), dateTimeFormatter))
                .build();
    }

    public static ViewStatsDto toViewStatsDto(StatsProjection statsProjection) {
        return ViewStatsDto.builder()
                .app(statsProjection.getApp())
                .uri(statsProjection.getUri())
                .hits(statsProjection.getHits())
                .build();
    }
}
