package com.br.obitus_manager.domain.city;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CityRequest {
    @NotNull(message = "O campo idState não pode ser nulo.")
    private UUID idState;

    @NotBlank(message = "O campo name não pode ser em branco.")
    private String name;
}
