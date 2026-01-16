package com.factory.machine_events.controller;

import com.factory.machine_events.dto.BatchIngestResponse;
import com.factory.machine_events.dto.EventIngestRequest;
import com.factory.machine_events.dto.EventStatsResponse;
import com.factory.machine_events.service.EventIngestService;
import com.factory.machine_events.service.EventStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventIngestController {

    private final EventIngestService eventIngestService;

    private final EventStatsService eventStatsService;

    @PostMapping("/batch")
    public ResponseEntity<BatchIngestResponse> ingestBatch(
            @RequestBody List<EventIngestRequest> requests
            ) {
        BatchIngestResponse response = eventIngestService.ingestBatch(requests);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/stats")
    public ResponseEntity<EventStatsResponse> getStats(
            @RequestParam("start")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Instant startTime,
            @RequestParam("end")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Instant endTime
            ) {
        return new ResponseEntity<>(eventStatsService.getStats(startTime,endTime),HttpStatus.OK);
    }
}
