package com.br.obitus_manager.domain.city.service;

import com.br.obitus_manager.domain.city.CityRequest;
import com.br.obitus_manager.domain.city.CityResponse;
import com.br.obitus_manager.domain.common.PageResponse;

import java.util.UUID;

public interface CityService {
    CityResponse upsert(UUID cityId, CityRequest request);

    PageResponse<CityResponse> findAllCities(
            final UUID stateId,
            final Integer page,
            final Integer size,
            final String sort
    );

    CityResponse findActiveByNameAndStateAcronym(String name, String stateAcronym);
}
