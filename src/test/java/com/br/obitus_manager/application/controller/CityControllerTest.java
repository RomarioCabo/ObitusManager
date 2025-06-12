package com.br.obitus_manager.application.controller;

import com.br.obitus_manager.ObitusManagerApplication;
import com.br.obitus_manager.application.exception.ErrorHttpResponseDto;
import com.br.obitus_manager.domain.city.CityRequest;
import com.br.obitus_manager.domain.city.CityResponse;
import com.br.obitus_manager.init.Init;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

@DirtiesContext(classMode = BEFORE_CLASS)
@ContextConfiguration(classes = ObitusManagerApplication.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("integtest")
class CityControllerTest extends Init {

    private static final String CITY_URL = "/cidade";

    @Test
    void shouldCreateCityWithSuccess() {
        CityRequest body = buildCityRequest("anyCity");

        ResponseEntity<CityResponse> response = createCity(body);

        assertEquals(CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals(body.getName(), response.getBody().getName());
        assertNotNull(response.getBody().getState());
        assertEquals(ACRE_STATE_ID, response.getBody().getState().getId());
    }

    @Test
    void shouldUpdateCityWithSuccess() {
        CityRequest bodyCreated = buildCityRequest("anyCity");
        CityRequest bodyUpdate = buildCityRequest("anyCityUpdate");

        UUID cityId = Objects.requireNonNull(createCity(bodyCreated).getBody()).getId();

        ResponseEntity<CityResponse> response
                = buildResponse(bodyUpdate, PUT, null, CITY_URL.concat("/").concat(cityId.toString()), CityResponse.class);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals(bodyUpdate.getName(), response.getBody().getName());
        assertNotNull(response.getBody().getState());
        assertEquals(ACRE_STATE_ID, response.getBody().getState().getId());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = "3c7f3a14-1c5e-4a3f-b26a-7a5b3ac9a2ae")
    void shouldFindCityWithSuccess(String stateIdStr) {
        UUID stateId = stateIdStr != null ? UUID.fromString(stateIdStr) : null;

        CityRequest bodyCreated = buildCityRequest("anyCity");
        createCity(bodyCreated);

        ResponseEntity<List<CityResponse>> response = buildResponse(
                null,
                "/cidades".concat(buildQueryParameters(stateId)),
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnUserAlreadyExistsError() {
        CityRequest body = buildCityRequest("anyCity");
        createCity(body);

        ResponseEntity<ErrorHttpResponseDto> response
                = buildResponse(body, POST, null, CITY_URL, ErrorHttpResponseDto.class);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(BAD_REQUEST.toString(), response.getBody().getCode());
        assertEquals("Ops! Ocorreu um erro", response.getBody().getTitle());
        assertEquals("Cidade já existente para esse estado na base de dados", response.getBody().getMessage());
    }

    @Test
    void shouldReturnStateNotFoundError() {
        CityRequest body = buildCityRequest("anyCity");
        body.setIdState(UUID.randomUUID());

        ResponseEntity<ErrorHttpResponseDto> response
                = buildResponse(body, POST, null, CITY_URL, ErrorHttpResponseDto.class);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(NOT_FOUND.toString(), response.getBody().getCode());
        assertEquals("Ops! Ocorreu um erro", response.getBody().getTitle());
        assertEquals("Estado informado não encontrado", response.getBody().getMessage());
    }

    private CityRequest buildCityRequest(String nameCity) {
        return CityRequest.builder()
                .idState(ACRE_STATE_ID)
                .name(nameCity)
                .build();
    }

    private ResponseEntity<CityResponse> createCity(CityRequest body) {
        return buildResponse(body, POST, null, CITY_URL, CityResponse.class);
    }

    private String buildQueryParameters(UUID stateId) {
        if (stateId == null) {
            return "";
        }

        return "?id_estado=" + stateId;
    }
}
