package com.factory.machine_events.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "machine_event",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "event_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MachineEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private String eventId;

    @Column(name = "machine_id", nullable = false)
    private String machineId;

    @Column(name = "event_time", nullable = false)
    private Instant eventTime;

    @Column(name = "received_time", nullable = false)
    private Instant receivedTime;

    @Column(name = "duration_ms", nullable = false)
    private long durationMs;

    @Column(name = "defect_count", nullable = false)
    private int defectCount;

    @Column(name = "payload_hash", nullable = false)
    private String payloadHash;
}

