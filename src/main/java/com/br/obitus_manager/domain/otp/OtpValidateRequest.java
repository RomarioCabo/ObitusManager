package com.br.obitus_manager.domain.otp;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OtpValidateRequest extends OtpCreateRequest {

    @NotBlank(message = "O campo code não pode ser vazio ou nulo.")
    private String code;
}
