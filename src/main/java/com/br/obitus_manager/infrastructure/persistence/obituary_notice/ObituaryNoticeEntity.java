package com.br.obitus_manager.infrastructure.persistence.obituary_notice;

import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeRequest;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeResponse;
import com.br.obitus_manager.infrastructure.persistence.city.CityEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notas_falecimento")
public class ObituaryNoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_nota_falecimento", updatable = false, unique = true, nullable = false)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_cidade")
    private CityEntity cityEntity;

    @Column(name = "nome_falecido", nullable = false)
    private String nameDeceased;

    @Column(name = "idade", nullable = false)
    private Integer age;

    @Column(name = "data_falecimento", nullable = false)
    private LocalDate dateDeceased;

    @Column(name = "local_velorio", nullable = false)
    private String wakeLocation;

    @Column(name = "local_sepultamento", nullable = false)
    private String burialLocation;

    @Column(name = "data_hora_sepultamento", nullable = false)
    private LocalDateTime dateTimeBurial;

    @Column(name = "biografia_resumida_falecido")
    private String briefBiographyDeceased;

    @Lob
    @Column(name = "foto", columnDefinition = "OID")
    private byte[] photo;

    public void setPhotoBase64(String base64) {
        if (base64 != null && !base64.isBlank()) {
            // remove prefixo tipo "data:image/jpeg;base64," se houver
            if (base64.contains(",")) {
                base64 = base64.split(",")[1];
            }
            this.photo = Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8));
        }
    }

    public String getUrlImageBase64(final String baseUrl) {
        if (this.photo != null && this.photo.length > 0) {
            return baseUrl.concat("api/v1/nota_falecimento/").concat(this.id.toString()).concat("/foto");
        }
        return null;
    }

    public ObituaryNoticeEntity(UUID idObituaryNotice, CityEntity cityEntity, ObituaryNoticeRequest request) {
        this.id = idObituaryNotice;
        this.cityEntity = cityEntity;
        this.nameDeceased = request.getNameDeceased().toUpperCase().trim();
        this.age = request.getAge();
        this.dateDeceased = request.getDateDeceased();
        this.wakeLocation = request.getWakeLocation().toUpperCase().trim();
        this.burialLocation = request.getBurialLocation().toUpperCase().trim();
        this.dateTimeBurial = request.getDateTimeBurial();
        this.briefBiographyDeceased = request.getBriefBiographyDeceased();
        this.setPhotoBase64(request.getImageBase64());
    }

    public ObituaryNoticeResponse toModel(final String baseUrl) {
        return ObituaryNoticeResponse.builder()
                .idObituaryNotice(this.id)
                .nameDeceased(this.nameDeceased)
                .age(this.age)
                .dateDeceased(this.dateDeceased)
                .wakeLocation(this.wakeLocation)
                .burialLocation(this.burialLocation)
                .dateTimeBurial(this.dateTimeBurial)
                .briefBiographyDeceased(this.briefBiographyDeceased)
                .urlImage(this.getUrlImageBase64(baseUrl))
                .build();
    }
}
