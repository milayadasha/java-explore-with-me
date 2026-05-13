package ru.practicum.ewm.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.server.model.EndpointHit;
import ru.practicum.ewm.server.model.StatsProjection;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT h.app as app, h.uri as uri, COUNT(h) as hits" +
            " FROM EndpointHit h" +
            " WHERE h.timestamp >= ?1" +
            " AND h.timestamp <= ?2" +
            " AND (?3 IS NULL OR h.uri IN ?3)" +
            " GROUP BY h.app, h.uri")
    List<StatsProjection> findNonUniqueStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT h.app as app, h.uri as uri, COUNT(DISTINCT h.ip) as hits" +
            " FROM EndpointHit h" +
            " WHERE h.timestamp >= ?1" +
            " AND h.timestamp <= ?2" +
            " AND (?3 IS NULL OR h.uri IN ?3)" +
            " GROUP BY h.app, h.uri")
    List<StatsProjection> findUniqueStats(LocalDateTime start, LocalDateTime end, List<String> uris);
}
