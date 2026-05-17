package com.br.obitus_manager.domain.obituary_notice;

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
public class ObituaryNoticeResponse {
    private UUID idObituaryNotice;
    private UUID idCity;
    private String nameDeceased;
    private Integer age;
    private LocalDate dateDeceased;
    private String wakeLocation;
    private String burialLocation;
    private LocalDateTime dateTimeBurial;
    private String briefBiographyDeceased;
    private Boolean hasPhoto;
  /** URL do endpoint GET /nota_falecimento/{id}/foto (sem bytes da imagem). */
    private String urlImage;
}
