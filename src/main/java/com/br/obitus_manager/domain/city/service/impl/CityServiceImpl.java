package com.br.obitus_manager.domain.city.service.impl;

import com.br.obitus_manager.application.exception.BadRequestException;
import com.br.obitus_manager.application.exception.EntityNotFoundException;
import com.br.obitus_manager.domain.DatabaseProvider;
import com.br.obitus_manager.domain.city.CityRequest;
import com.br.obitus_manager.domain.city.CityResponse;
import com.br.obitus_manager.domain.city.service.CityService;
import com.br.obitus_manager.domain.util.PagedUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final PagedUtil pagedUtil;
    private final DatabaseProvider databaseProvider;

    @Override
    public CityResponse upsert(final UUID cityId, final CityRequest request) {
        boolean existsCity = databaseProvider.existsCityByNameAndState(request.getName(), request.getIdState());
        if (existsCity) {
            throw new BadRequestException("Cidade já existente para esse estado na base de dados");
        }

        CityResponse cityResponse = databaseProvider.saveCity(request, cityId);
        if (cityResponse == null) {
            throw new EntityNotFoundException("Estado informado não encontrado");
        }

        return cityResponse;
    }

    @Override
    public List<CityResponse> findAllCities(final UUID stateId) {
        final Pageable pageable = pagedUtil.getPageable(null, null);

        return databaseProvider.findAllCities(null, buildAdvancedFilters(stateId), pageable, "name");
    }

    private static Map<String, Map<String, Object>> buildAdvancedFilters(final UUID stateId) {
        final Map<String, Map<String, Object>> advancedFilters = new HashMap<>();
        Optional.ofNullable(stateId).ifPresent(id -> advancedFilters.put("stateEntity", buildMapFilter(stateId)));

        return advancedFilters.isEmpty() ? null : advancedFilters;
    }

    private static Map<String, Object> buildMapFilter(final Object value) {
        final Map<String, Object> mapTenantId = new HashMap<>();
        mapTenantId.put("id", value);
        return mapTenantId;
    }
}
