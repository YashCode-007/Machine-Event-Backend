package com.factory.machine_events.service;

import com.factory.machine_events.dto.BatchIngestResponse;
import com.factory.machine_events.dto.EventIngestRequest;
import com.factory.machine_events.dto.RejectionReason;
import com.factory.machine_events.entity.MachineEvent;
import com.factory.machine_events.repository.MachineEventRepository;
import com.factory.machine_events.util.PayloadHashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventIngestService {

    private final MachineEventRepository machineEventRepository;

    @Transactional
    public BatchIngestResponse ingestBatch(List<EventIngestRequest> requests) {

        int accepted = 0;
        int deduped = 0;
        int updated = 0;
        int rejected = 0;

        List<RejectionReason> rejections = new ArrayList<>();

        for(EventIngestRequest request : requests) {

            //validation
            Optional<String> validationError = validate(request);
            if(validationError.isPresent()) {
                rejected++;
                rejections.add(new RejectionReason(request.getEventId(),validationError.get()));
                continue;
            }

            //Prepare Hash and Receive Time
            String payloadHash = PayloadHashUtil.generateHash(request);
            Instant receivedTime = Instant.now();

            //Check if Event Already Exist or not
            Optional<MachineEvent> existingOpt = machineEventRepository.findByEventId(request.getEventId());

            //If Empty
            if(existingOpt.isEmpty()) {
                MachineEvent event = MachineEvent.builder()
                        .eventId(request.getEventId())
                        .machineId(request.getMachineId())
                        .eventTime(request.getEventTime())
                        .receivedTime(receivedTime)
                        .defectCount(request.getDefectCount())
                        .durationMs(request.getDurationMs())
                        .payloadHash(payloadHash)
                        .build();

                machineEventRepository.save(event);
                accepted++;
                continue;
            }

            //If Exists -> DEDUPE or UPDATE
            MachineEvent existing = existingOpt.get();

            //DEDUPE
            if(existing.getPayloadHash().equals(payloadHash)) {
                deduped++;
                continue;
            }

            //If Payload Different, compare received Time
            if(receivedTime.isAfter(existing.getReceivedTime())) {

                existing.setMachineId(request.getMachineId());
                existing.setEventTime(request.getEventTime());
                existing.setReceivedTime(receivedTime);
                existing.setDefectCount(request.getDefectCount());
                existing.setDurationMs(request.getDurationMs());
                existing.setPayloadHash(payloadHash);

                machineEventRepository.save(existing);
                updated++;

            }
        }
        return BatchIngestResponse.builder()
                .accepted(accepted)
                .rejected(rejected)
                .deduped(deduped)
                .updated(updated)
                .rejections(rejections)
                .build();

    }

    //Validation logic
    private Optional<String> validate(EventIngestRequest request) {

        //Duration validation
        if (request.getDurationMs() < 0 || request.getDurationMs() > 21600000) {
            return Optional.of("INVALID_DURATION");
        }

        //Future Time Validation
        Instant nowPlus15 = Instant.now().plusSeconds(15*60);
        if(request.getEventTime().isAfter(nowPlus15)) {
            return Optional.of("EVENT_TIME_IN_FUTURE");
        }

        return Optional.empty();
    }

}

