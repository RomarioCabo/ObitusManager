package com.br.obitus_manager.init;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
public abstract class Init {

    public static final String URL = "http://localhost:";
    public static final String API_V1 = "/api/v1";

    public static final UUID ACRE_STATE_ID = UUID.fromString("3c7f3a14-1c5e-4a3f-b26a-7a5b3ac9a2ae");

    @LocalServerPort
    public int port;

    @Autowired
    private Flyway flyway;

    @BeforeEach
    protected void setup() {
        flyway.clean();
        flyway.migrate();
    }

    @Autowired
    private TestRestTemplate testRestTemplate;

    protected <Request, Response> ResponseEntity<Response> buildResponse(final Request body, final HttpMethod httpMethod,
                                                                         final String token, final String url,
                                                                         final Class<Response> clazz) {
        return testRestTemplate.exchange(String.format("%s%s%s%s", URL, port, API_V1, url), httpMethod,
                new HttpEntity<>(body, setHeaders(token)), clazz);
    }

    protected <Request, Response> ResponseEntity<List<Response>> buildResponse(final List<Request> body,
                                                                               final HttpMethod httpMethod,
                                                                               final String token,
                                                                               final String url,
                                                                               final ParameterizedTypeReference<List<Response>> responseType) {
        return testRestTemplate.exchange(
                String.format("%s%s%s%s", URL, port, API_V1, url),
                httpMethod,
                new HttpEntity<>(body, setHeaders(token)),
                responseType
        );
    }

    protected <T> ResponseEntity<List<T>> buildResponse(final String token, final String url,
                                                        final ParameterizedTypeReference<List<T>> responseType) {
        return testRestTemplate.exchange(String.format("%s%s%s%s", URL, port, API_V1, url), HttpMethod.GET,
                new HttpEntity<>(setHeaders(token)), responseType);
    }

    private HttpHeaders setHeaders(final String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCEPT, APPLICATION_JSON_VALUE);
        headers.set(CONTENT_TYPE, APPLICATION_JSON_VALUE);

        if (token != null) {
            headers.set(AUTHORIZATION, "Bearer " + token);
        }

        return headers;
    }
}
