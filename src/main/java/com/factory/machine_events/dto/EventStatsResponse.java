package com.factory.machine_events.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventStatsResponse {

    private long totalEvents;
    private long totalDefects;
    private long avgDurationMs;
}
