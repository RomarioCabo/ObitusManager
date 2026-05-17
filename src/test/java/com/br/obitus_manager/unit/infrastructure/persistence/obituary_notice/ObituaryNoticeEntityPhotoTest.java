package com.br.obitus_manager.unit.infrastructure.persistence.obituary_notice;

import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeRequest;
import com.br.obitus_manager.infrastructure.persistence.city.CityEntity;
import com.br.obitus_manager.infrastructure.persistence.obituary_notice.ObituaryNoticeEntity;
import com.br.obitus_manager.testsupport.ObituaryNoticeTestData;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

class ObituaryNoticeEntityPhotoTest {

    @Test
    void setPhotoBase64NullDoesNotChangeExistingPhoto() {
        ObituaryNoticeEntity entity = new ObituaryNoticeEntity();
        entity.setPhoto(new byte[]{1, 2, 3});
        entity.setPhotoContentType(IMAGE_PNG_VALUE);

        entity.setPhotoBase64(null);

        assertNotNull(entity.getPhoto());
        assertEquals(3, entity.getPhoto().length);
    }

    @Test
    void setPhotoBase64EmptyRemovesPhoto() {
        ObituaryNoticeEntity entity = new ObituaryNoticeEntity();
        entity.setPhoto(new byte[]{1});
        entity.setPhotoContentType(IMAGE_PNG_VALUE);

        entity.setPhotoBase64("");

        assertNull(entity.getPhoto());
        assertNull(entity.getPhotoContentType());
    }

    @Test
    void setPhotoBase64DecodesPngAndSetsContentType() {
        ObituaryNoticeEntity entity = new ObituaryNoticeEntity();

        entity.setPhotoBase64(ObituaryNoticeTestData.MINIMAL_PNG_DATA_URL);

        assertNotNull(entity.getPhoto());
        assertTrue(entity.getPhoto().length > 0);
        assertEquals(IMAGE_PNG_VALUE, entity.getPhotoContentType());
    }

    @Test
    void buildPhotoUrlWhenPhotoPresent() {
        UUID id = UUID.randomUUID();
        ObituaryNoticeEntity entity = new ObituaryNoticeEntity();
        entity.setId(id);
        entity.setPhoto(new byte[]{1});
        entity.setPhotoContentType(IMAGE_PNG_VALUE);
        ReflectionTestUtils.setField(entity, "hasPhoto", true);

        String url = entity.buildPhotoUrl("http://localhost:8080/");

        assertNotNull(url);
        assertTrue(url.endsWith("/foto"));
        assertTrue(url.contains(id.toString()));
    }

    @Test
    void constructorFromRequestUppercasesName() {
        CityEntity city = CityEntity.builder().id(UUID.randomUUID()).build();
        ObituaryNoticeRequest request = ObituaryNoticeRequest.builder()
                .idCity(city.getId())
                .nameDeceased("joão silva")
                .age(70)
                .dateDeceased(java.time.LocalDate.of(2026, 1, 1))
                .wakeLocation("capela")
                .burialLocation("cemitério")
                .dateTimeBurial(java.time.LocalDateTime.of(2026, 1, 2, 9, 0))
                .build();

        ObituaryNoticeEntity entity = new ObituaryNoticeEntity(UUID.randomUUID(), city, request);

        assertEquals("JOÃO SILVA", entity.getNameDeceased());
        assertEquals("CAPELA", entity.getWakeLocation());
    }
}
