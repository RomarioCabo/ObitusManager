package com.br.obitus_manager.integration.support;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import com.br.obitus_manager.domain.city.CityRequest;
import com.br.obitus_manager.domain.city.CityResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 * Base para testes de integração HTTP (profile {@code integtest}, H2 em memória).
 * {@link #resetDatabase()} executa Flyway clean + migrate antes de cada teste — cenário isolado.
 */
@Component
public abstract class IntegrationTestSupport {

    public static final String URL = "http://localhost:";
    public static final String API_V1 = "/api/v1";

    public static final UUID ACRE_STATE_ID = UUID.fromString("3c7f3a14-1c5e-4a3f-b26a-7a5b3ac9a2ae");

    @LocalServerPort
    public int port;

    @Autowired
    private Flyway flyway;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    protected void resetDatabase() {
        flyway.clean();
        flyway.migrate();
    }

    protected String apiUrl(String path) {
        return URL + port + API_V1 + path;
    }

    protected <Request, Response> ResponseEntity<Response> buildResponse(
            final Request body,
            final HttpMethod httpMethod,
            final String token,
            final String url,
            final Class<Response> clazz
    ) {
        return testRestTemplate.exchange(
                apiUrl(url),
                httpMethod,
                new HttpEntity<>(body, jsonHeaders(token)),
                clazz
        );
    }

    protected <Request, Response> ResponseEntity<List<Response>> buildResponse(
            final List<Request> body,
            final HttpMethod httpMethod,
            final String token,
            final String url,
            final ParameterizedTypeReference<List<Response>> responseType
    ) {
        return testRestTemplate.exchange(
                apiUrl(url),
                httpMethod,
                new HttpEntity<>(body, jsonHeaders(token)),
                responseType
        );
    }

    protected <T> ResponseEntity<T> buildResponse(
            final String token,
            final String url,
            final ParameterizedTypeReference<T> responseType
    ) {
        return testRestTemplate.exchange(
                apiUrl(url),
                HttpMethod.GET,
                new HttpEntity<>(jsonHeaders(token)),
                responseType
        );
    }

    /** Cria cidade no Acre (estado de seed) para cenários de integração. */
    protected UUID createCityInAcre(final String cityName) {
        CityRequest request = CityRequest.builder()
                .idState(ACRE_STATE_ID)
                .name(cityName)
                .build();

        ResponseEntity<CityResponse> response = buildResponse(
                request,
                POST,
                null,
                "/cidade",
                CityResponse.class
        );

        if (!CREATED.equals(response.getStatusCode()) || response.getBody() == null) {
            throw new IllegalStateException("Falha ao criar cidade de teste: " + response.getStatusCode());
        }

        return response.getBody().getId();
    }

    protected ResponseEntity<byte[]> getBytes(final String url) {
        return testRestTemplate.exchange(
                apiUrl(url),
                HttpMethod.GET,
                new HttpEntity<>(jsonHeaders(null)),
                byte[].class
        );
    }

    private HttpHeaders jsonHeaders(final String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCEPT, APPLICATION_JSON_VALUE);
        headers.set(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        if (token != null) {
            headers.set(AUTHORIZATION, "Bearer " + token);
        }
        return headers;
    }
}
