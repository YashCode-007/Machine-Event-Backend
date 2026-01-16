package com.factory.machine_events.service;

import com.factory.machine_events.dto.EventStatsResponse;
import com.factory.machine_events.repository.MachineEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class EventStatsService {

    private final MachineEventRepository machineEventRepository;

    public EventStatsResponse getStats(Instant start, Instant end) {

        long totalEvents = machineEventRepository.countByEventTimeBetween(start,end);
        long totalDefects = machineEventRepository.sumDefects(start,end);
        long avgDurationMs = machineEventRepository.avgDuration(start,end).longValue();

        return new EventStatsResponse(
                totalEvents,
                totalDefects,
                avgDurationMs
        );
    }
}
