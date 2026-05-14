package ru.practicum.ewm.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.server.exception.NotFoundException;
import ru.practicum.ewm.server.exception.ValidationException;
import ru.practicum.ewm.server.mapper.StatsMapper;
import ru.practicum.ewm.server.model.EndpointHit;
import ru.practicum.ewm.server.model.StatsProjection;
import ru.practicum.ewm.server.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final EndpointHitRepository endpointHitRepository;
    private final DateTimeFormatter dateTimeFormatter;

    @Override
    @Transactional(readOnly = false)
    public EndpointHitDto addHit(EndpointHitDto endpointHitDto) {
        log.info("Начался процесс сохранения запроса к эндпоинту в БД");
        EndpointHit endpointHit = StatsMapper.toEndpointHit(endpointHitDto, dateTimeFormatter);
        if (endpointHit == null) {
            throw new NotFoundException("Запрос к эндпоинту для добавления не найден");
        }
        log.info("Детали запроса: app = {}, uri = {}, ip = {})",
                endpointHit.getApp(), endpointHit.getUri(), endpointHit.getIp());

        EndpointHit savedEndpointHit = endpointHitRepository.save(endpointHit);

        log.info("Запрос сохранён. Детали запроса: id = {}, app = {}, uri = {}, ip = {}, timestamp = {})",
                endpointHit.getId(), endpointHit.getApp(), endpointHit.getUri(),
                endpointHit.getIp(), endpointHit.getTimestamp());
        return StatsMapper.toEndpointHitDto(savedEndpointHit, dateTimeFormatter);
    }

    @Override
    public List<ViewStatsDto> getViewStats(String startStr, String endStr, List<String> uris, boolean unique) {
        log.info("Начался процесс обработки запроса статистики. " +
                "Детали запроса: start = {}, end = {}, uris = {}, unique = {}", startStr, endStr, uris, unique);
        LocalDateTime start = LocalDateTime.parse(startStr, dateTimeFormatter);
        LocalDateTime end = LocalDateTime.parse(endStr, dateTimeFormatter);
        validateViewStatRequest(start, end, uris);

        if (uris != null && uris.isEmpty()) {
            log.warn("Список uri пустой");
            return Collections.emptyList();
        }

        List<StatsProjection> statsList = unique ? endpointHitRepository.findUniqueStats(start, end, uris)
                : endpointHitRepository.findNonUniqueStats(start, end, uris);

        if (statsList.isEmpty()) {
            log.info("По данным фильтрам найдено 0 записей");
            return Collections.emptyList();
        }

        log.info("По данным фильтрам найдено {} записей", statsList.size());
        return statsList.stream()
                .map(StatsMapper::toViewStatsDto)
                .toList();
    }

    private void validateViewStatRequest(LocalDateTime start, LocalDateTime end, List<String> uris) {
        if (start.isAfter(end)) {
            String dateException = "Дата начала не должна быть после даты окончания: start = " + start +
                    ", end = " + end;
            log.error(dateException);
            throw new ValidationException(dateException);
        }
        log.info("Проверка входных данных запроса пройдена");
    }
}
