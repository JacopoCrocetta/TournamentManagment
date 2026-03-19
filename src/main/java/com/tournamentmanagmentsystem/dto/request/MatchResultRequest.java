package com.tournamentmanagmentsystem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchResultRequest {
    private Map<String, Object> score;
    private UUID winnerId;
    
    @Builder.Default
    private Boolean forfeit = false;
}
