package com.br.obitus_manager.application.controller;

import com.br.obitus_manager.application.exception.ErrorHttpResponseDto;
import com.br.obitus_manager.domain.common.PageResponse;
import com.br.obitus_manager.domain.state.StateRequest;
import com.br.obitus_manager.domain.state.StateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Estados", description = "Estados REST Controller")
public interface StateController {

    @ApiResponse(
            responseCode = "200",
            description = "Estados atualizados com sucesso.",
            content = @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = StateResponse.class))
            )
    )
    @PutMapping(
            value = "/estados",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    ResponseEntity<List<StateResponse>> saveAllStates(
            @RequestBody List<StateRequest> stateRequests
    );

    @Operation(
            summary = "Listar estados (paginado)",
            description = """
                    Filtro opcional: ativo (true/false). \
                    Ordenação: name, acronym, active (ex.: acronym,asc)."""
    )
    @ApiResponse(
            responseCode = "200",
            description = "Página de estados.",
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
    @GetMapping(value = "/estados", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<PageResponse<StateResponse>> findAllStatesByActive(
            @Parameter(description = "Filtrar por estado atuante")
            @RequestParam(name = "ativo", required = false) Boolean active,
            @Parameter(description = "Índice da página (0-based)", example = "0")
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @Parameter(description = "Tamanho da página", example = "10")
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @Parameter(description = "Ordenação: campo,direção", example = "acronym,asc")
            @RequestParam(required = false) String sort
    );
}
