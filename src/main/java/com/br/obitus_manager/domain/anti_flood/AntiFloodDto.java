package com.br.obitus_manager.domain.anti_flood;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AntiFloodDto {
    private UUID id;
    private String email;
    private int attempts;
    private LocalDateTime blockStart;
    private LocalDateTime blockEnd;
    private boolean isBlocked;
    private int blockedLevel;
}
