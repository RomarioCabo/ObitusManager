package com.br.obitus_manager.application.controller;

import com.br.obitus_manager.application.exception.ErrorHttpResponseDto;
import com.br.obitus_manager.domain.common.PageResponse;
import com.br.obitus_manager.domain.user.UserRequest;
import com.br.obitus_manager.domain.user.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Usuário", description = "Usuário REST Controller")
public interface UserController {

    @ApiResponse(
            responseCode = "201",
            description = "Usuário criado com sucesso.",
            content = @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Bad request.",
            content = @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorHttpResponseDto.class)
            )
    )
    @PostMapping(
            value = "/usuario",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    ResponseEntity<UserResponse> save(
            @RequestBody UserRequest request
    );

    @ApiResponse(
            responseCode = "200",
            description = "Usuário alterado com sucesso.",
            content = @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Bad request.",
            content = @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorHttpResponseDto.class)
            )
    )
    @PutMapping(
            value = "/usuario/{id_usuario}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    ResponseEntity<UserResponse> update(
            HttpServletRequest httpServletRequest,
            @RequestBody UserRequest request,
            @PathVariable("id_usuario") UUID userId
    );

    @Operation(
            summary = "Listar usuários (paginado)",
            description = "Ordenação: name, email, createdAt (ex.: name,asc)."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Página de usuários.",
            content = @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PageResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Bad request.",
            content = @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorHttpResponseDto.class)
            )
    )
    @GetMapping(value = "/usuarios", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<PageResponse<UserResponse>> findAllUsers(
            @Parameter(description = "Índice da página (0-based)", example = "0")
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @Parameter(description = "Tamanho da página", example = "10")
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @Parameter(description = "Ordenação: campo,direção", example = "name,asc")
            @RequestParam(required = false) String sort
    );

    @ApiResponse(
            responseCode = "200",
            description = "Usuário encontrado.",
            content = @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Bad request.",
            content = @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorHttpResponseDto.class)
            )
    )
    @GetMapping(value = "/usuario/{id_usuario}", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<UserResponse> findById(
            HttpServletRequest httpServletRequest,
            @PathVariable("id_usuario") UUID userId
    );
}
