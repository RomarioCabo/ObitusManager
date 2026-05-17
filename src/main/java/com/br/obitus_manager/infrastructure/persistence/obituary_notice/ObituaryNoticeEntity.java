package com.br.obitus_manager.infrastructure.persistence.obituary_notice;

import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeRequest;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeResponse;
import com.br.obitus_manager.domain.util.ImageMimeUtils;
import com.br.obitus_manager.infrastructure.persistence.city.CityEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;
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

    @Basic(fetch = FetchType.LAZY)
    @Lob
    @Column(name = "foto", columnDefinition = "OID")
    private byte[] photo;

    /** Indica presença de foto sem carregar o LOB (PostgreSQL OID). */
    @Formula("(foto IS NOT NULL)")
    private boolean hasPhoto;

    @Column(name = "foto_content_type", length = 64)
    private String photoContentType;

    /**
     * Atualiza foto a partir do Base64 do request.
     * <ul>
     *   <li>{@code null} — não altera foto (uso com merge no update)</li>
     *   <li>vazio — remove a foto</li>
     *   <li>com dados — grava bytes e MIME</li>
     * </ul>
     */
    public void setPhotoBase64(String base64) {
        if (base64 == null) {
            return;
        }
        if (base64.isBlank()) {
            this.photo = null;
            this.photoContentType = null;
            return;
        }

        String mimeFromPrefix = ImageMimeUtils.parseMimeFromDataUrlPrefix(base64);
        if (base64.contains(",")) {
            base64 = base64.split(",", 2)[1];
        }
        this.photo = Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8));
        if (ImageMimeUtils.isSupportedImageMime(mimeFromPrefix)) {
            this.photoContentType = mimeFromPrefix;
        } else {
            this.photoContentType = ImageMimeUtils.detectFromBytes(this.photo);
        }
    }

    public String resolvePhotoContentType() {
        if (photoContentType != null && !photoContentType.isBlank()) {
            return photoContentType;
        }
        return ImageMimeUtils.detectFromBytes(photo);
    }

    public String buildPhotoUrl(final String baseUrl) {
        if (!hasPhoto || this.id == null) {
            return null;
        }
        return baseUrl.concat("api/v1/nota_falecimento/").concat(this.id.toString()).concat("/foto");
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
                .idCity(this.cityEntity != null ? this.cityEntity.getId() : null)
                .nameDeceased(this.nameDeceased)
                .age(this.age)
                .dateDeceased(this.dateDeceased)
                .wakeLocation(this.wakeLocation)
                .burialLocation(this.burialLocation)
                .dateTimeBurial(this.dateTimeBurial)
                .briefBiographyDeceased(this.briefBiographyDeceased)
                .hasPhoto(this.hasPhoto)
                .urlImage(this.buildPhotoUrl(baseUrl))
                .build();
    }
}
