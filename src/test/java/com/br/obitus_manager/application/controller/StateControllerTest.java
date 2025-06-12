package com.br.obitus_manager.application.controller;

import com.br.obitus_manager.ObitusManagerApplication;
import com.br.obitus_manager.domain.state.StateRequest;
import com.br.obitus_manager.domain.state.StateResponse;
import com.br.obitus_manager.init.Init;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

@DirtiesContext(classMode = BEFORE_CLASS)
@ContextConfiguration(classes = ObitusManagerApplication.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("integtest")
class StateControllerTest extends Init {

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
    void shouldReturnAllStatesWithFilterOrNot(Boolean active, int expectedQtdStates) {
        ResponseEntity<List<StateResponse>> response = getStates(active);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedQtdStates, response.getBody().size());
    }

    private void validatingWhetherStateOfAcreIsInactiveBeforeUpdate() {
        StateResponse stateAcreBeforeUpdate = getStates(null).getBody().stream()
                .filter(state -> state.getId().equals(ACRE_STATE_ID))
                .findFirst()
                .orElse(null);

        assertNotNull(stateAcreBeforeUpdate);
        assertEquals(ACRE_STATE_ID, stateAcreBeforeUpdate.getId());
        assertEquals("Acre", stateAcreBeforeUpdate.getName());
        assertEquals("AC", stateAcreBeforeUpdate.getAcronym());
        assertFalse(stateAcreBeforeUpdate.getActive());
    }

    private ResponseEntity<List<StateResponse>> getStates(Boolean active) {
        return buildResponse(null, buildUrl(active), new ParameterizedTypeReference<>() {
        });
    }

    private StateRequest buildState() {
        return StateRequest.builder()
                .id(ACRE_STATE_ID)
                .active(true)
                .build();
    }

    private String buildUrl(Boolean active) {
        if (active != null) {
            return STATE_URL.concat("?ativo=").concat(active.toString());
        }

        return STATE_URL;
    }

    private static Stream<Arguments> activeFilterProvider() {
        return Stream.of(
                Arguments.of(null, 27),
                Arguments.of(true, 1),
                Arguments.of(false, 26)
        );
    }
}
