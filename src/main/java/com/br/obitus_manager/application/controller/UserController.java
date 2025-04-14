package com.br.obitus_manager.application.controller;

import com.br.obitus_manager.application.exception.ErrorHttpResponseDto;
import com.br.obitus_manager.domain.user.UserRequest;
import com.br.obitus_manager.domain.user.UserResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tags(@Tag(name = "Usuário", description = "Usuário REST Controller"))
public interface UserController {

    @ApiResponse(
            responseCode = "201",
            description = "Usuário criado com sucesso.",
            content = {@Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserResponse.class))
            })
    @ApiResponse(
            responseCode = "400",
            description = "Bad request.",
            content = {@Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorHttpResponseDto.class))
            })
    @PostMapping(
            value = "usuario",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    ResponseEntity<UserResponse> save(@RequestBody UserRequest request);

    @ApiResponse(
            responseCode = "200",
            description = "Usuário alterado com sucesso.",
            content = {@Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserResponse.class))
            })
    @ApiResponse(
            responseCode = "400",
            description = "Bad request.",
            content = {@Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorHttpResponseDto.class))
            })
    @PutMapping(
            value = "usuario/{id_usuario}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    ResponseEntity<UserResponse> update(
            HttpServletRequest httpServletRequest,
            @RequestBody UserRequest request,
            @PathVariable("id_usuario") UUID userId
    );

    @ApiResponse(
            responseCode = "200",
            description = "Usuário(s) encontrado.",
            content = {@Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))
            })
    @ApiResponse(
            responseCode = "400",
            description = "Bad request.",
            content = {@Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorHttpResponseDto.class))
            })
    @GetMapping(value = "usuarios", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<UserResponse>> findAllUsers();

    @ApiResponse(
            responseCode = "200",
            description = "Usuário encontrado.",
            content = {@Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserResponse.class))
            })
    @ApiResponse(
            responseCode = "400",
            description = "Bad request.",
            content = {@Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserResponse.class))
            })
    @GetMapping(value = "/usuario/{id_usuario}", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<UserResponse> findById(
            HttpServletRequest httpServletRequest,
            @PathVariable("id_usuario") UUID userId
    );
}
