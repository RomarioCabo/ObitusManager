package com.br.obitus_manager.application.controller;

import com.br.obitus_manager.ObitusManagerApplication;
import com.br.obitus_manager.application.exception.ErrorHttpResponseDto;
import com.br.obitus_manager.domain.user.UserRequest;
import com.br.obitus_manager.domain.user.UserResponse;
import com.br.obitus_manager.init.Init;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

@DirtiesContext(classMode = BEFORE_CLASS)
@ContextConfiguration(classes = ObitusManagerApplication.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("integtest")
class UserControllerTest extends Init {

    private static final String CREATE_USER_URL = "/usuario";
    private static final String FIND_ALL_USERS = "/usuarios";

    private static final String NAME = "anyName";
    private static final String EMAIL = "any@email.com";
    private static final String PASSWORD = "anyPassword@";

    @Test
    void shouldCreateUserWithSuccess() {
        UserRequest body = buildRequest(NAME, EMAIL, PASSWORD);

        ResponseEntity<UserResponse> response = buildResponse(body, POST, null, CREATE_USER_URL, UserResponse.class);

        assertEquals(CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals(body.getName(), response.getBody().getName());
        assertEquals(body.getEmail(), response.getBody().getEmail());
        assertNotNull(response.getBody().getPassword());
    }

    @ParameterizedTest
    @CsvSource({
            "           , new@email.com,                , anyName    , new@email.com",
            "           ,              ,                , anyName    , any@email.com",
            "updatedName, new@email.com, updatedPassword, updatedName, new@email.com",
    })
    void shouldUpdateUserWithSuccess(String updatedName, String updatedEmail, String updatedPassword, String expectedName, String expectedEmail) {
        UserRequest body = buildRequest(updatedName, updatedEmail, updatedPassword);

        UserResponse userCreated = createUser();

        ResponseEntity<UserResponse> response = buildResponse(body, PUT, null, CREATE_USER_URL.concat("/")
                .concat(userCreated.getId().toString()), UserResponse.class);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userCreated.getId(), response.getBody().getId());
        assertEquals(expectedName, response.getBody().getName());
        assertEquals(expectedEmail, response.getBody().getEmail());

        if (updatedPassword == null) {
            assertEquals(userCreated.getPassword(), response.getBody().getPassword());
        } else {
            assertNotEquals(userCreated.getPassword(), response.getBody().getPassword());
        }
    }

    @Test
    void shouldReturnAllUsersWithSuccess() {
        createUser();

        ResponseEntity<List<UserResponse>> response = buildResponse(null, FIND_ALL_USERS,
                new ParameterizedTypeReference<>() {
                });

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldReturnFindUserByIdWithSuccess() {
        UserResponse user = createUser();

        ResponseEntity<UserResponse> response
                = buildResponse(null, GET, null, CREATE_USER_URL.concat("/").concat(user.getId().toString()), UserResponse.class);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user.getId(), response.getBody().getId());
        assertEquals(user.getName(), response.getBody().getName());
        assertEquals(user.getEmail(), response.getBody().getEmail());
        assertNotNull(response.getBody().getPassword());
    }

    @Test
    void shouldReturnBadRequestWhenNotSendMandatoryFields() {
        UserRequest body = buildRequest(NAME, null, null);

        ResponseEntity<ErrorHttpResponseDto> response
                = buildResponse(body, POST, null, CREATE_USER_URL, ErrorHttpResponseDto.class);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(BAD_REQUEST.toString(), response.getBody().getCode());
        assertEquals("Ops! Ocorreu um erro", response.getBody().getTitle());
        assertEquals("O campo email é obrigatório.\nO campo senha é obrigatório.", response.getBody().getMessage());
    }

    @ParameterizedTest
    @MethodSource("invalidPasswordProvider")
    void shouldReturnBadRequestWhenPasswordNotMeetRequirements(String password, String expectedErrorMessage) {
        UserRequest body = buildRequest(NAME, EMAIL, password);

        ResponseEntity<ErrorHttpResponseDto> response
                = buildResponse(body, POST, null, CREATE_USER_URL, ErrorHttpResponseDto.class);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(BAD_REQUEST.toString(), response.getBody().getCode());
        assertEquals("Ops! Ocorreu um erro", response.getBody().getTitle());
        assertEquals(expectedErrorMessage, response.getBody().getMessage());
    }

    @Test
    void shouldReturnBadRequestWhenEmailAlreadyExists() {
        UserRequest body = buildRequest(NAME, EMAIL, PASSWORD);

        UserResponse userCreated = createUser();

        ResponseEntity<ErrorHttpResponseDto> response = buildResponse(body, PUT, null, CREATE_USER_URL.concat("/")
                .concat(userCreated.getId().toString()), ErrorHttpResponseDto.class);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(BAD_REQUEST.toString(), response.getBody().getCode());
        assertEquals("Ops! Ocorreu um erro", response.getBody().getTitle());
        assertEquals("Usuário com o e-mail: ".concat(EMAIL).concat(" já existe!"), response.getBody().getMessage());
    }

    private UserResponse createUser() {
        return buildResponse(buildRequest(NAME, EMAIL, PASSWORD), POST, null, CREATE_USER_URL,
                UserResponse.class).getBody();
    }

    private UserRequest buildRequest(String name, String email, String password) {
        UserRequest.UserRequestBuilder builder = UserRequest.builder();

        if (name != null) {
            builder.name(name);
        }

        if (email != null) {
            builder.email(email);
        }

        if (password != null) {
            builder.password(password);
        }

        return builder.build();
    }

    private static Stream<Arguments> invalidPasswordProvider() {
        return Stream.of(
                Arguments.of(
                        "teste@",
                        "A senha deve conter no mínimo 8 caracteres!\nA senha deve conter no mínimo uma letra maiúscula!"
                ),
                Arguments.of(
                        "testesenhamaiorqueoitocaracteressemletramaiuscula",
                        "A senha deve conter no mínimo um caracter especial!\nA senha deve conter no mínimo uma letra maiúscula!"
                ),
                Arguments.of(
                        "TesteSenhaMaiorQueOitoCaracteresSemCaracteresEspecial",
                        "A senha deve conter no mínimo um caracter especial!"
                )
        );
    }
}
