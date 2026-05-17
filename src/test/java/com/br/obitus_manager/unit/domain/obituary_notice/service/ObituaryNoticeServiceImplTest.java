package com.br.obitus_manager.unit.domain.obituary_notice.service;

import com.br.obitus_manager.application.exception.EntityNotFoundException;
import com.br.obitus_manager.domain.DatabaseProvider;
import com.br.obitus_manager.domain.common.PageResponse;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticePhoto;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeRequest;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeResponse;
import com.br.obitus_manager.domain.obituary_notice.service.impl.ObituaryNoticeServiceImpl;
import com.br.obitus_manager.domain.util.PagedUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObituaryNoticeServiceImplTest {

    @InjectMocks
    private ObituaryNoticeServiceImpl service;

    @Mock
    private DatabaseProvider databaseProvider;

    @Mock
    private PagedUtil pagedUtil;

    @Test
    void upsertReturnsResponseWhenCityExists() {
        UUID cityId = UUID.randomUUID();
        ObituaryNoticeRequest request = ObituaryNoticeRequest.builder().idCity(cityId).build();
        ObituaryNoticeResponse expected = ObituaryNoticeResponse.builder()
                .idObituaryNotice(UUID.randomUUID())
                .idCity(cityId)
                .build();

        when(databaseProvider.saveObituaryNotice(request, null)).thenReturn(expected);

        ObituaryNoticeResponse result = service.upsert(request, null);

        assertEquals(expected, result);
    }

    @Test
    void upsertThrowsWhenCityNotFound() {
        ObituaryNoticeRequest request = ObituaryNoticeRequest.builder()
                .idCity(UUID.randomUUID())
                .build();

        when(databaseProvider.saveObituaryNotice(request, null)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> service.upsert(request, null));
    }

    @Test
    void findBuildsFiltersAndReturnsPageResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        ObituaryNoticeResponse item = ObituaryNoticeResponse.builder()
                .nameDeceased("MARIA")
                .build();
        Page<ObituaryNoticeResponse> page = new PageImpl<>(List.of(item), pageable, 1);

        when(pagedUtil.getObituaryPageable(0, 10, null)).thenReturn(pageable);
        when(databaseProvider.findObituaryNotice(
                argThat(m -> m.containsKey("nameDeceased")),
                isNull(),
                eq(pageable),
                eq("nameDeceased")
        )).thenReturn(page);

        PageResponse<ObituaryNoticeResponse> result = service.find(
                "Maria",
                null,
                null,
                0,
                10,
                null
        );

        assertEquals(1, result.getTotalElements());
        assertEquals("MARIA", result.getContent().get(0).getNameDeceased());
    }

    @Test
    void findPassesCityFilterAsAdvancedFilter() {
        UUID cityId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 5);
        Page<ObituaryNoticeResponse> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(pagedUtil.getObituaryPageable(0, 5, "dateDeceased,desc")).thenReturn(pageable);
        when(databaseProvider.findObituaryNotice(
                anyMap(),
                argThat(advanced -> advanced != null
                        && advanced.containsKey("cityEntity")
                        && cityId.equals(advanced.get("cityEntity").get("id"))),
                eq(pageable),
                eq("nameDeceased")
        )).thenReturn(emptyPage);

        PageResponse<ObituaryNoticeResponse> result = service.find(
                null,
                cityId,
                LocalDate.of(2026, 5, 10),
                0,
                5,
                "dateDeceased,desc"
        );

        assertEquals(0, result.getTotalElements());
        verify(pagedUtil).getObituaryPageable(0, 5, "dateDeceased,desc");
    }

    @Test
    void getPhotoDelegatesToDatabaseProvider() {
        UUID id = UUID.randomUUID();
        ObituaryNoticePhoto photo = new ObituaryNoticePhoto(new byte[]{1, 2}, "image/png");

        when(databaseProvider.getPhotoByIdObituaryNoticeId(id)).thenReturn(photo);

        assertEquals(photo, service.getPhotoByIdObituaryNoticeId(id));
    }
}
