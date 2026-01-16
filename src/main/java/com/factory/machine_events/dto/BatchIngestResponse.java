package com.factory.machine_events.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BatchIngestResponse {

    private int accepted;
    private int deduped;
    private int updated;
    private int rejected;

    @Builder.Default
    private List<RejectionReason> rejections = new ArrayList<>();
}
