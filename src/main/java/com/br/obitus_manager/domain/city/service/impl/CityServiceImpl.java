package com.br.obitus_manager.domain.city.service.impl;

import com.br.obitus_manager.application.exception.BadRequestException;
import com.br.obitus_manager.application.exception.EntityNotFoundException;
import com.br.obitus_manager.domain.DatabaseProvider;
import com.br.obitus_manager.domain.city.CityRequest;
import com.br.obitus_manager.domain.city.CityResponse;
import com.br.obitus_manager.domain.city.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

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
    public List<CityResponse> findAllCitiesByState(final UUID stateId) {
        return databaseProvider.findAllCitiesByState(stateId);
    }
}
