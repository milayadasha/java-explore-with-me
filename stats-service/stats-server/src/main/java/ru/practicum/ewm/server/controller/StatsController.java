package ru.practicum.ewm.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.server.service.StatsService;
import java.util.List;


@RestController
public class StatsController {
    @Autowired
    public StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity<Void> addHit(@RequestBody EndpointHitDto hitDto) {
        statsService.addHit(hitDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsDto>> getStats(@RequestParam String start,
                                                       @RequestParam String end,
                                                       @RequestParam(required = false) List<String> uris,
                                                       @RequestParam(defaultValue = "false") boolean unique) {
        return ResponseEntity.ok().body(statsService.getViewStats(start, end, uris, unique));
    }
}
