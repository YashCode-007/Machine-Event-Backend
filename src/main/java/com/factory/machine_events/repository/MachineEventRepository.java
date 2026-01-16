package com.factory.machine_events.repository;

import com.factory.machine_events.entity.MachineEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface MachineEventRepository extends JpaRepository<MachineEvent, Long> {

    Optional<MachineEvent> findByEventId(String eventId);

    long countByEventTimeBetween(Instant start, Instant end);

    @Query("SELECT COALESCE(SUM(e.defectCount),0) FROM MachineEvent e WHERE e.eventTime BETWEEN :start AND :end ")
    long sumDefects(
            @Param("start") Instant start,
            @Param("end") Instant end
            );

    @Query("SELECT COALESCE(AVG(e.durationMs),0) FROM MachineEvent e WHERE e.eventTime BETWEEN :start AND :end ")
    Double avgDuration(
            @Param("start") Instant start,
            @Param("end") Instant end
            );
}