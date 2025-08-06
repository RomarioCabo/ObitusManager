package com.br.obitus_manager.infrastructure.persistence.anti_flood;

import com.br.obitus_manager.domain.anti_flood.AntiFloodDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "anti_flood")
public class AntiFloodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_antiflood", updatable = false, unique = true, nullable = false)
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "tentativas", nullable = false)
    private int attempts;

    @Column(name = "inicio_bloqueio")
    private LocalDateTime blockStart;

    @Column(name = "fim_bloqueio")
    private LocalDateTime blockEnd;

    @Column(name = "nivel_bloqueio", nullable = false)
    private int blockedLevel;

    private boolean isBlocked() {
        return blockEnd != null && blockEnd.isAfter(LocalDateTime.now());
    }

    public AntiFloodDto toDto() {
        return AntiFloodDto.builder()
                .id(this.id)
                .email(this.email)
                .attempts(this.attempts)
                .blockStart(this.blockStart)
                .blockEnd(this.blockEnd)
                .isBlocked(this.isBlocked())
                .blockedLevel(this.blockedLevel)
                .build();
    }
}
