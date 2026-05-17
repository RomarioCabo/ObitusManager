package com.br.obitus_manager.application.controller;

import com.br.obitus_manager.application.exception.ErrorHttpResponseDto;
import com.br.obitus_manager.domain.city.CityRequest;
import com.br.obitus_manager.domain.city.CityResponse;
import com.br.obitus_manager.domain.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Cidade", description = "Cidade REST Controller")
public interface CityController {

    @ApiResponse(
            responseCode = "201",
            description = "Cidade criada com sucesso.",
            content = @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CityResponse.class)
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
    @ApiResponse(
            responseCode = "404",
            description = "Not found.",
            content = @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorHttpResponseDto.class)
            )
    )
    @PostMapping(
            value = "/cidade",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    ResponseEntity<CityResponse> save(@Valid @RequestBody CityRequest request);

    @ApiResponse(
            responseCode = "200",
            description = "Cidade alterada com sucesso.",
            content = @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CityResponse.class)
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
    @ApiResponse(
            responseCode = "404",
            description = "Not found.",
            content = @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorHttpResponseDto.class)
            )
    )
    @PutMapping(
            value = "/cidade/{id_cidade}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    ResponseEntity<CityResponse> update(
            @Valid @RequestBody CityRequest request,
            @PathVariable("id_cidade") UUID cityId
    );

    @Operation(
            summary = "Listar cidades (paginado)",
            description = """
                    Filtro opcional: id_estado. \
                    Ordenação: name (ex.: name,asc)."""
    )
    @ApiResponse(
            responseCode = "200",
            description = "Página de cidades.",
            content = @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PageResponse.class)
            )
    )
    @GetMapping(value = "/cidades", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<PageResponse<CityResponse>> findAllCategoriesByIdUser(
            @Parameter(description = "Filtrar por estado (UF)")
            @RequestParam(name = "id_estado", required = false) UUID idState,
            @Parameter(description = "Índice da página (0-based)", example = "0")
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @Parameter(description = "Tamanho da página", example = "10")
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @Parameter(description = "Ordenação: campo,direção", example = "name,asc")
            @RequestParam(required = false) String sort
    );
}
