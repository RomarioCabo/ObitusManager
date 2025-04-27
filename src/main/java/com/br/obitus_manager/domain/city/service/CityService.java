package com.br.obitus_manager.domain.city.service;

import com.br.obitus_manager.domain.city.CityRequest;
import com.br.obitus_manager.domain.city.CityResponse;

import java.util.List;
import java.util.UUID;

public interface CityService {
    CityResponse upsert(UUID cityId, CityRequest request);

    List<CityResponse> findAllCities(final UUID stateId);
}
