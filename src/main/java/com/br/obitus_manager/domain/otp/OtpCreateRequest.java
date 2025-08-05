package com.br.obitus_manager.domain.otp;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OtpCreateRequest {

    @Email(message = "Digite um e-mail válido.")
    @NotBlank(message = "O campo e-mail não pode ser vazio ou nulo.")
    private String email;
}
