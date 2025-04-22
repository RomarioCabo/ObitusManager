package com.br.obitus_manager.domain.obituary_notice.service.impl;

import com.br.obitus_manager.application.exception.EntityNotFoundException;
import com.br.obitus_manager.domain.DatabaseProvider;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeRequest;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeResponse;
import com.br.obitus_manager.domain.obituary_notice.service.ObituaryNoticeService;
import com.br.obitus_manager.domain.util.PagedUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ObituaryNoticeServiceImpl implements ObituaryNoticeService {

    private final DatabaseProvider databaseProvider;
    private final PagedUtil pagedUtil;

    @Override
    public ObituaryNoticeResponse upsert(final ObituaryNoticeRequest request, final UUID obituaryNoticeId) {
        ObituaryNoticeResponse obituaryNoticeResponse = databaseProvider.saveObituaryNotice(request, obituaryNoticeId);
        if (obituaryNoticeResponse == null) {
            throw new EntityNotFoundException("Cidade informada não encontrada");
        }

        return obituaryNoticeResponse;
    }

    @Override
    public byte[] getPhotoByIdObituaryNoticeId(final UUID obituaryNoticeId) {
        return databaseProvider.getPhotoByIdObituaryNoticeId(obituaryNoticeId);
    }

    @Override
    public List<ObituaryNoticeResponse> find(final String nameDeceased, final UUID idCity, LocalDate dateDeceased) {
        final Map<String, Object> filters = new HashMap<>();
        Optional.ofNullable(nameDeceased).ifPresent(id -> filters.put("nameDeceased", nameDeceased));
        Optional.ofNullable(dateDeceased).ifPresent(date -> filters.put("dateDeceased", dateDeceased));

        final Pageable pageable = pagedUtil.getPageable(null, null);

        return databaseProvider.findObituaryNotice(filters, buildAdvancedFilters(idCity), pageable, "nameDeceased");
    }

    private static Map<String, Map<String, Object>> buildAdvancedFilters(final UUID idCity) {
        final Map<String, Map<String, Object>> advancedFilters = new HashMap<>();
        Optional.ofNullable(idCity).ifPresent(id -> advancedFilters.put("cityEntity", buildMapFilter("id", idCity)));

        return advancedFilters.isEmpty() ? null : advancedFilters;
    }

    private static Map<String, Object> buildMapFilter(final String key, final Object value) {
        final Map<String, Object> mapTenantId = new HashMap<>();
        mapTenantId.put(key, value);
        return mapTenantId;
    }
}
