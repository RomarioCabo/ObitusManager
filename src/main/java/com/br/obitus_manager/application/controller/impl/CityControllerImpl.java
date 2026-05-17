package com.br.obitus_manager.application.controller.impl;

import com.br.obitus_manager.application.controller.CityController;
import com.br.obitus_manager.application.util.ControllerUtils;
import com.br.obitus_manager.domain.city.CityRequest;
import com.br.obitus_manager.domain.city.CityResponse;
import com.br.obitus_manager.domain.city.service.CityService;
import com.br.obitus_manager.domain.common.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CityControllerImpl extends ControllerUtils implements CityController {

    private final CityService cityService;

    @Override
    public ResponseEntity<CityResponse> save(CityRequest request) {
        log.info("Saving city: {}", request);

        CityResponse response = cityService.upsert(null, request);

        log.info("City saved: {}", response);

        return ResponseEntity.created(generateUriBuild(response.getId())).body(response);
    }

    @Override
    public ResponseEntity<CityResponse> update(CityRequest request, UUID cityId) {
        log.info("Updating city: {}", request);

        CityResponse response = cityService.upsert(cityId, request);

        log.info("City updated: {}", response);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CityResponse> findByNameAndStateAcronym(
            final String name,
            final String stateAcronym
    ) {
        log.info("find city by name={} uf={}", name, stateAcronym);

        CityResponse response = cityService.findActiveByNameAndStateAcronym(name, stateAcronym);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PageResponse<CityResponse>> findAllCategoriesByIdUser(
            final UUID idState,
            final Integer page,
            final Integer size,
            final String sort
    ) {
        log.info("get cities by state: {}", idState);

        PageResponse<CityResponse> response = cityService.findAllCities(idState, page, size, sort);

        log.info("cities page {}: {} item(s)", response.getPage(), response.getContent().size());

        return ResponseEntity.ok(response);
    }
}
