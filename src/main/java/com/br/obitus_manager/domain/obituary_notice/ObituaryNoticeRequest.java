package com.br.obitus_manager.domain.obituary_notice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ObituaryNoticeRequest {
    @NotNull(message = "O campo idCity não pode ser nulo.")
    private UUID idCity;
    @NotBlank(message = "O campo nameDeceased não pode ser em branco.")
    private String nameDeceased;
    @NotNull(message = "O campo age não pode ser nulo.")
    private Integer age;
    @NotNull(message = "O campo dateDeceased não pode ser nulo.")
    private LocalDate dateDeceased;
    @NotBlank(message = "O campo wakeLocation não pode ser em branco.")
    private String wakeLocation;
    @NotBlank(message = "O campo burialLocation não pode ser em branco.")
    private String burialLocation;
    @NotNull(message = "O campo dateTimeBurial não pode ser nulo.")
    private LocalDateTime dateTimeBurial;
    private String briefBiographyDeceased;
    @ToString.Exclude
    private String imageBase64;
}
