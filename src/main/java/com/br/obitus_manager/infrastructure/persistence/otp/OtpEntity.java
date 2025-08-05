package com.br.obitus_manager.infrastructure.persistence.otp;

import com.br.obitus_manager.domain.otp.OtpDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "one_time_password")
public class OtpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_otp", updatable = false, unique = true, nullable = false)
    private UUID id;

    @Column(name = "codigo_hash", nullable = false)
    private String codeHash;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "usado", nullable = false)
    private boolean used;

    @Column(name = "tentativas", nullable = false)
    private int attempts = 0;

    public boolean isExpired() {
        return createdAt.plusMinutes(5).isBefore(LocalDateTime.now());
    }

    public boolean isValid() {
        return !used && !isExpired() && attempts < 3;
    }

    public boolean isExceededAttempts() {
        return attempts >= 3;
    }

    public OtpDto toDto() {
        return OtpDto.builder()
                .id(this.id)
                .codeHash(this.codeHash)
                .email(this.email)
                .createdAt(this.createdAt)
                .isExpired(this.isExpired())
                .isValid(this.isValid())
                .isExceededAttempts(this.isExceededAttempts())
                .build();
    }
}
