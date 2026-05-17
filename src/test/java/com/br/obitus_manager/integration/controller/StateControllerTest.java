package com.br.obitus_manager.integration.controller;

import com.br.obitus_manager.domain.common.PageResponse;
import com.br.obitus_manager.domain.state.StateRequest;
import com.br.obitus_manager.domain.state.StateResponse;
import com.br.obitus_manager.integration.support.IntegrationTest;
import com.br.obitus_manager.integration.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.OK;

@IntegrationTest
class StateControllerTest extends IntegrationTestSupport {

    private static final String STATE_URL = "/estados";

    @Test
    void shouldUpdateState() {
        validatingWhetherStateOfAcreIsInactiveBeforeUpdate();

        ResponseEntity<List<StateResponse>> response = buildResponse(Collections.singletonList(buildState()),
                HttpMethod.PUT, null, STATE_URL, new ParameterizedTypeReference<>() {
                });

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());

        List<StateResponse> states = response.getBody();
        StateResponse filteredState = states
                .stream()
                .filter(state -> state.getId().equals(ACRE_STATE_ID))
                .findFirst()
                .orElse(null);

        assertNotNull(filteredState);
        assertEquals(ACRE_STATE_ID, filteredState.getId());
        assertEquals("Acre", filteredState.getName());
        assertEquals("AC", filteredState.getAcronym());
        assertTrue(filteredState.getActive());
    }

    @ParameterizedTest
    @MethodSource("activeFilterProvider")
    void shouldReturnPaginatedStatesWithFilterOrNot(Boolean active, int expectedTotal) {
        ResponseEntity<PageResponse<StateResponse>> response = getStates(active, 0, 100);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedTotal, response.getBody().getTotalElements());
        assertEquals(expectedTotal, response.getBody().getContent().size());
    }

    @Test
    void shouldReturnPaginatedListWithDefaultPageSize() {
        ResponseEntity<PageResponse<StateResponse>> response = getStates(null, 0, 10);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(27, response.getBody().getTotalElements());
        assertEquals(10, response.getBody().getContent().size());
        assertEquals(0, response.getBody().getPage());
        assertEquals(10, response.getBody().getSize());
        assertTrue(response.getBody().isFirst());
        assertFalse(response.getBody().isLast());
    }

    private void validatingWhetherStateOfAcreIsInactiveBeforeUpdate() {
        StateResponse stateAcreBeforeUpdate = getStates(null, 0, 100).getBody().getContent().stream()
                .filter(state -> state.getId().equals(ACRE_STATE_ID))
                .findFirst()
                .orElse(null);

        assertNotNull(stateAcreBeforeUpdate);
        assertEquals(ACRE_STATE_ID, stateAcreBeforeUpdate.getId());
        assertEquals("Acre", stateAcreBeforeUpdate.getName());
        assertEquals("AC", stateAcreBeforeUpdate.getAcronym());
        assertFalse(stateAcreBeforeUpdate.getActive());
    }

    private ResponseEntity<PageResponse<StateResponse>> getStates(Boolean active, int page, int size) {
        return buildResponse(null, buildUrl(active, page, size), new ParameterizedTypeReference<>() {
        });
    }

    private StateRequest buildState() {
        return StateRequest.builder()
                .id(ACRE_STATE_ID)
                .active(true)
                .build();
    }

    private String buildUrl(Boolean active, int page, int size) {
        StringBuilder url = new StringBuilder(STATE_URL)
                .append("?page=").append(page)
                .append("&size=").append(size);

        if (active != null) {
            url.append("&ativo=").append(active);
        }

        return url.toString();
    }

    private static Stream<Arguments> activeFilterProvider() {
        return Stream.of(
                Arguments.of(null, 27),
                Arguments.of(true, 1),
                Arguments.of(false, 26)
        );
    }
}
