package com.br.obitus_manager.domain.otp;

import lombok.*;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OtpResponse {
    private String pin;
}
