package com.br.obitus_manager.application.controller;

import com.br.obitus_manager.application.exception.ErrorHttpResponseDto;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeRequest;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@Tags(@Tag(name = "Notas de Falecimento", description = "Notas de Falecimento REST Controller"))
public interface ObituaryNoticeController {

    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Nota de Falecimento criada com sucesso.",
                    content = {@Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ObituaryNoticeResponse.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request.",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorHttpResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found.",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorHttpResponseDto.class)
                    )
            )
    })
    @PostMapping(
            value = "nota_falecimento",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    ResponseEntity<ObituaryNoticeResponse> save(@Valid @RequestBody ObituaryNoticeRequest request);

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Nota de Falecimento alterada com sucesso.",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ObituaryNoticeResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request.",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorHttpResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found.",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorHttpResponseDto.class)
                    )
            )
    })
    @PutMapping(
            value = "nota_falecimento/{id_nota_falecimento}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    ResponseEntity<ObituaryNoticeResponse> update(
            @Valid @RequestBody ObituaryNoticeRequest request,
            @PathVariable("id_nota_falecimento") UUID obituaryNoticeId
    );

    @ApiResponse(
            responseCode = "200",
            description = "Imagem da Nota de Falecimento recuperada com sucesso.",
            content = @Content(mediaType = IMAGE_JPEG_VALUE)
    )
    @GetMapping("/nota_falecimento/{obituaryNoticeId}/foto")
    ResponseEntity<byte[]> getPhoto(@PathVariable UUID obituaryNoticeId);

    @ApiResponse(
            responseCode = "200",
            description = "Nomes encontrados.",
            content = {@Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = ObituaryNoticeResponse.class)))
            })
    @GetMapping(value = "/notas_falecimento", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<ObituaryNoticeResponse>> find(
            @RequestParam(required = false, name = "nome_falecido") String nameDeceased,
            @RequestParam(required = false, name = "id_cidade") UUID idCity,
            @RequestParam(required = false, name = "data_falecimento") LocalDate dateDeceased
    );
}
