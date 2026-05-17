package com.br.obitus_manager.application.controller;

import com.br.obitus_manager.application.exception.ErrorHttpResponseDto;
import com.br.obitus_manager.domain.common.PageResponse;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeRequest;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@Tag(name = "Notas de Falecimento", description = "Notas de Falecimento REST Controller")
public interface ObituaryNoticeController {

    @ApiResponse(
            responseCode = "201",
            description = "Nota de Falecimento criada com sucesso.",
            content = @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ObituaryNoticeResponse.class)
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
            value = "/nota_falecimento",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    ResponseEntity<ObituaryNoticeResponse> save(
            @Valid @RequestBody ObituaryNoticeRequest request
    );

    @ApiResponse(
            responseCode = "200",
            description = "Nota de Falecimento alterada com sucesso.",
            content = @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ObituaryNoticeResponse.class)
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
            value = "/nota_falecimento/{id_nota_falecimento}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    ResponseEntity<ObituaryNoticeResponse> update(
            @Valid @RequestBody ObituaryNoticeRequest request,
            @PathVariable("id_nota_falecimento") UUID obituaryNoticeId
    );

    @ApiResponse(
            responseCode = "200",
            description = "Imagem da Nota de Falecimento recuperada com sucesso (Content-Type conforme formato salvo).",
            content = {
                    @Content(mediaType = IMAGE_JPEG_VALUE),
                    @Content(mediaType = IMAGE_PNG_VALUE),
                    @Content(mediaType = "image/webp")
            }
    )
    @ApiResponse(
            responseCode = "204",
            description = "Nota sem foto ou foto inexistente."
    )
    @GetMapping("/nota_falecimento/{obituaryNoticeId}/foto")
    ResponseEntity<byte[]> getPhoto(
            @PathVariable UUID obituaryNoticeId
    );

    @Operation(
            summary = "Listar notas de falecimento (paginado)",
            description = """
                    Retorna notas paginadas. A foto deve ser obtida em \
                    GET /nota_falecimento/{id}/foto (campo urlImage / hasPhoto). \
                    Ordenação: nameDeceased, dateDeceased, dateTimeBurial (ex.: dateDeceased,desc)."""
    )
    @ApiResponse(
            responseCode = "200",
            description = "Página de notas de falecimento.",
            content = @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PageResponse.class)
            )
    )
    @GetMapping(value = "/notas_falecimento", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<PageResponse<ObituaryNoticeResponse>> find(
            @Parameter(description = "Filtro por nome do falecido")
            @RequestParam(required = false, name = "nome_falecido") String nameDeceased,
            @RequestParam(required = false, name = "id_cidade") UUID idCity,
            @RequestParam(required = false, name = "data_falecimento") LocalDate dateDeceased,
            @Parameter(description = "Índice da página (0-based)", example = "0")
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @Parameter(description = "Tamanho da página", example = "10")
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @Parameter(description = "Ordenação: campo,direção", example = "nameDeceased,asc")
            @RequestParam(required = false) String sort
    );
}