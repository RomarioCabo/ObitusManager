package com.br.obitus_manager.domain.otp;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OtpDto {
    private UUID id;
    private String codeHash;
    private String email;
    private LocalDateTime createdAt;
    private boolean isExpired;
    private boolean isValid;
    private boolean isExceededAttempts;
}
