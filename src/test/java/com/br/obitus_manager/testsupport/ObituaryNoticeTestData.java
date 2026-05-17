package com.br.obitus_manager.testsupport;

import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/** Dados mínimos e determinísticos para testes de notas de falecimento. */
public final class ObituaryNoticeTestData {

    /** PNG 1x1 em Base64 puro (sem prefixo data URL). */
    public static final String MINIMAL_PNG_BASE64 =
            "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8/5+hHgAHggJ/PchI7wAAAABJRU5ErkJggg==";

    public static final String MINIMAL_PNG_DATA_URL =
            "data:image/png;base64," + MINIMAL_PNG_BASE64;

    private ObituaryNoticeTestData() {
    }

    public static ObituaryNoticeRequest defaultRequest(final UUID cityId) {
        return ObituaryNoticeRequest.builder()
                .idCity(cityId)
                .nameDeceased("João Silva")
                .age(80)
                .dateDeceased(LocalDate.of(2026, 5, 10))
                .wakeLocation("Capela Central")
                .burialLocation("Cemitério Municipal")
                .dateTimeBurial(LocalDateTime.of(2026, 5, 11, 10, 0))
                .briefBiographyDeceased("Biografia resumida.")
                .build();
    }

    public static ObituaryNoticeRequest requestWithName(final UUID cityId, final String name) {
        ObituaryNoticeRequest base = defaultRequest(cityId);
        return ObituaryNoticeRequest.builder()
                .idCity(cityId)
                .nameDeceased(name)
                .age(base.getAge())
                .dateDeceased(base.getDateDeceased())
                .wakeLocation(base.getWakeLocation())
                .burialLocation(base.getBurialLocation())
                .dateTimeBurial(base.getDateTimeBurial())
                .briefBiographyDeceased(base.getBriefBiographyDeceased())
                .build();
    }
}
