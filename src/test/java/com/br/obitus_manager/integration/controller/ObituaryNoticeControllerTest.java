package com.br.obitus_manager.integration.controller;

import com.br.obitus_manager.application.exception.ErrorHttpResponseDto;
import com.br.obitus_manager.domain.common.PageResponse;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeRequest;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeResponse;
import com.br.obitus_manager.integration.support.IntegrationTest;
import com.br.obitus_manager.integration.support.IntegrationTestSupport;
import com.br.obitus_manager.testsupport.ObituaryNoticeTestData;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@IntegrationTest
class ObituaryNoticeControllerTest extends IntegrationTestSupport {

    private static final String OBITUARY_URL = "/nota_falecimento";
    private static final String OBITUARIES_URL = "/notas_falecimento";

    @Test
    void shouldCreateObituaryNoticeWithPhoto() {
        UUID cityId = createCityInAcre("CidadeFoto");
        ObituaryNoticeRequest body = ObituaryNoticeTestData.defaultRequest(cityId);
        body.setImageBase64(ObituaryNoticeTestData.MINIMAL_PNG_BASE64);

        ResponseEntity<ObituaryNoticeResponse> response =
                buildResponse(body, POST, null, OBITUARY_URL, ObituaryNoticeResponse.class);

        assertEquals(CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        UUID noticeId = response.getBody().getIdObituaryNotice();
        assertNotNull(noticeId);

        ResponseEntity<byte[]> photo = getBytes(OBITUARY_URL + "/" + noticeId + "/foto");
        assertEquals(OK, photo.getStatusCode());
        assertNotNull(photo.getBody());
        assertTrue(photo.getBody().length > 0);
    }

    @Test
    void shouldCreateObituaryNoticeWithoutPhoto() {
        UUID cityId = createCityInAcre("CidadeSemFoto");
        ObituaryNoticeRequest body = ObituaryNoticeTestData.defaultRequest(cityId);

        ResponseEntity<ObituaryNoticeResponse> response =
                buildResponse(body, POST, null, OBITUARY_URL, ObituaryNoticeResponse.class);

        assertEquals(CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(Boolean.TRUE.equals(response.getBody().getHasPhoto()));
    }

    @Test
    void shouldReturnPaginatedList() {
        UUID cityId = createCityInAcre("CidadePagina");
        createNotice(cityId, "Maria Paginada");
        createNotice(cityId, "José Paginado");

        ResponseEntity<PageResponse<ObituaryNoticeResponse>> response = buildResponse(
                null,
                OBITUARIES_URL + "?page=0&size=1",
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals(0, response.getBody().getPage());
        assertEquals(1, response.getBody().getSize());
        assertTrue(response.getBody().getTotalElements() >= 2);
        assertTrue(response.getBody().getTotalPages() >= 2);
        assertTrue(response.getBody().isFirst());
        assertFalse(response.getBody().isLast());
    }

    @Test
    void shouldFilterByDeceasedName() {
        UUID cityId = createCityInAcre("CidadeNome");
        createNotice(cityId, "Ana Específica");
        createNotice(cityId, "Pedro Outro");

        ResponseEntity<PageResponse<ObituaryNoticeResponse>> response = buildResponse(
                null,
                OBITUARIES_URL + "?nome_falecido=ANA&page=0&size=10",
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertTrue(response.getBody().getContent().get(0).getNameDeceased().contains("ANA"));
        assertFalse(response.getBody().getContent().get(0).getNameDeceased().contains("PEDRO"));
    }

    @Test
    void shouldFilterByCityId() {
        UUID cityA = createCityInAcre("CidadeA");
        UUID cityB = createCityInAcre("CidadeB");
        createNotice(cityA, "Nota Cidade A");
        createNotice(cityB, "Nota Cidade B");

        ResponseEntity<PageResponse<ObituaryNoticeResponse>> response = buildResponse(
                null,
                OBITUARIES_URL + "?id_cidade=" + cityA + "&page=0&size=10",
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(cityA, response.getBody().getContent().get(0).getIdCity());
    }

    @Test
    void shouldFilterByDateDeceased() {
        UUID cityId = createCityInAcre("CidadeData");
        ObituaryNoticeRequest body = ObituaryNoticeTestData.defaultRequest(cityId);
        body.setDateDeceased(LocalDate.of(2026, 3, 15));
        createNotice(body);

        ObituaryNoticeRequest other = ObituaryNoticeTestData.defaultRequest(cityId);
        other.setDateDeceased(LocalDate.of(2026, 1, 1));
        createNotice(other);

        ResponseEntity<PageResponse<ObituaryNoticeResponse>> response = buildResponse(
                null,
                OBITUARIES_URL + "?data_falecimento=2026-03-15&page=0&size=10",
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(LocalDate.of(2026, 3, 15), response.getBody().getContent().get(0).getDateDeceased());
    }

    @Test
    void shouldReturnPhotoWithCorrectContentType() {
        UUID cityId = createCityInAcre("CidadeGetFoto");
        ObituaryNoticeRequest body = ObituaryNoticeTestData.defaultRequest(cityId);
        body.setImageBase64(ObituaryNoticeTestData.MINIMAL_PNG_BASE64);

        UUID noticeId = Objects.requireNonNull(
                buildResponse(body, POST, null, OBITUARY_URL, ObituaryNoticeResponse.class).getBody()
        ).getIdObituaryNotice();

        ResponseEntity<byte[]> photoResponse = getBytes(OBITUARY_URL + "/" + noticeId + "/foto");

        assertEquals(OK, photoResponse.getStatusCode());
        assertNotNull(photoResponse.getBody());
        assertTrue(photoResponse.getBody().length > 0);
        assertEquals(IMAGE_PNG_VALUE, photoResponse.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE));
    }

    @Test
    void shouldReturn204WhenPhotoAbsent() {
        UUID cityId = createCityInAcre("CidadeSemFotoGet");
        UUID noticeId = Objects.requireNonNull(
                createNotice(cityId, "Sem Foto").getBody()
        ).getIdObituaryNotice();

        ResponseEntity<byte[]> photoResponse = getBytes(OBITUARY_URL + "/" + noticeId + "/foto");

        assertEquals(NO_CONTENT, photoResponse.getStatusCode());
    }

    @Test
    void shouldUpdateAndPreservePhotoWhenImageOmitted() {
        UUID cityId = createCityInAcre("CidadeUpdatePreserve");
        ObituaryNoticeRequest create = ObituaryNoticeTestData.defaultRequest(cityId);
        create.setImageBase64(ObituaryNoticeTestData.MINIMAL_PNG_BASE64);

        UUID noticeId = Objects.requireNonNull(
                buildResponse(create, POST, null, OBITUARY_URL, ObituaryNoticeResponse.class).getBody()
        ).getIdObituaryNotice();

        ObituaryNoticeRequest update = ObituaryNoticeTestData.defaultRequest(cityId);
        update.setNameDeceased("Nome Atualizado");
        update.setImageBase64(null);

        ResponseEntity<ObituaryNoticeResponse> updateResponse = buildResponse(
                update,
                PUT,
                null,
                OBITUARY_URL + "/" + noticeId,
                ObituaryNoticeResponse.class
        );

        assertEquals(OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());

        ResponseEntity<byte[]> photoResponse = getBytes(OBITUARY_URL + "/" + noticeId + "/foto");
        assertEquals(OK, photoResponse.getStatusCode());
        assertNotNull(photoResponse.getBody());
        assertTrue(photoResponse.getBody().length > 0);
    }

    @Test
    void shouldRemovePhotoWhenImageBase64Empty() {
        UUID cityId = createCityInAcre("CidadeRemoveFoto");
        ObituaryNoticeRequest create = ObituaryNoticeTestData.defaultRequest(cityId);
        create.setImageBase64(ObituaryNoticeTestData.MINIMAL_PNG_BASE64);

        UUID noticeId = Objects.requireNonNull(
                buildResponse(create, POST, null, OBITUARY_URL, ObituaryNoticeResponse.class).getBody()
        ).getIdObituaryNotice();

        ObituaryNoticeRequest update = ObituaryNoticeTestData.defaultRequest(cityId);
        update.setImageBase64("");

        ResponseEntity<ObituaryNoticeResponse> updateResponse = buildResponse(
                update,
                PUT,
                null,
                OBITUARY_URL + "/" + noticeId,
                ObituaryNoticeResponse.class
        );

        assertEquals(OK, updateResponse.getStatusCode());
        assertFalse(Boolean.TRUE.equals(updateResponse.getBody().getHasPhoto()));

        ResponseEntity<byte[]> photoResponse = getBytes(OBITUARY_URL + "/" + noticeId + "/foto");
        assertEquals(NO_CONTENT, photoResponse.getStatusCode());
    }

    @Test
    void shouldReturn404WhenCityNotFound() {
        ObituaryNoticeRequest body = ObituaryNoticeTestData.defaultRequest(UUID.randomUUID());

        ResponseEntity<ErrorHttpResponseDto> response =
                buildResponse(body, POST, null, OBITUARY_URL, ErrorHttpResponseDto.class);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Cidade informada não encontrada", response.getBody().getMessage());
    }

    @Test
    void shouldReturnBadRequestWhenMandatoryFieldsMissing() {
        UUID cityId = createCityInAcre("CidadeValidacao");
        ObituaryNoticeRequest body = ObituaryNoticeRequest.builder()
                .idCity(cityId)
                .build();

        ResponseEntity<ErrorHttpResponseDto> response =
                buildResponse(body, POST, null, OBITUARY_URL, ErrorHttpResponseDto.class);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("nameDeceased"));
    }

    private ResponseEntity<ObituaryNoticeResponse> createNotice(final UUID cityId, final String name) {
        return createNotice(ObituaryNoticeTestData.requestWithName(cityId, name));
    }

    private ResponseEntity<ObituaryNoticeResponse> createNotice(final ObituaryNoticeRequest body) {
        return buildResponse(body, POST, null, OBITUARY_URL, ObituaryNoticeResponse.class);
    }
}
