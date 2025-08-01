package com.br.obitus_manager.domain.otp;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static com.br.obitus_manager.domain.otp.OtpRequestType.CREATE;

@Setter
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OtpCreateRequest implements OtpRequest {

    private final OtpRequestType type = CREATE;

    @NotBlank(message = "O campo templateId não pode ser vazio ou nulo.")
    private String templateId;

    @Email(message = "Digite um e-mail válido.")
    @NotBlank(message = "O campo e-mail não pode ser vazio ou nulo.")
    private String email;

    @Override
    public OtpRequestType getType() {
        return type;
    }
}
