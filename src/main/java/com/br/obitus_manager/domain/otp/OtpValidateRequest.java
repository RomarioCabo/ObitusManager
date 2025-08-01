package com.br.obitus_manager.domain.otp;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

import static com.br.obitus_manager.domain.otp.OtpRequestType.VALIDATE;

@Setter
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OtpValidateRequest extends OtpCreateRequest implements OtpRequest {

    private final OtpRequestType type = VALIDATE;

    @Min(value = 1000, message = "O PIN deve ter no mínimo 4 dígitos.")
    @Max(value = 999999, message = "O PIN deve ter no máximo 6 dígitos.")
    @NotNull(message = "O campo PIN não pode ser vazio ou nulo.")
    private Integer pin;

    @NotNull(message = "O campo HASH não pode ser vazio ou nulo.")
    private UUID hash;

    @Override
    public OtpRequestType getType() {
        return type;
    }
}
