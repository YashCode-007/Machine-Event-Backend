package com.factory.machine_events.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventIngestRequest {

    @NotBlank
    private String eventId;

    @NotNull
    private Instant eventTime;

    @NotBlank
    private String machineId;

    @Min(0)
    private long durationMs;

    private int defectCount;
}
