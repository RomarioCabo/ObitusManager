package com.br.obitus_manager.application.controller;

import com.br.obitus_manager.application.exception.ErrorHttpResponseDto;
import com.br.obitus_manager.domain.state.StateResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tags(@Tag(name = "Estados", description = "Estados REST Controller"))
public interface StateController {

    @ApiResponse(
            responseCode = "200",
            description = "Estados(s) encontrado.",
            content = {@Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = StateResponse.class)))
            })
    @ApiResponse(
            responseCode = "400",
            description = "Bad request.",
            content = {@Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorHttpResponseDto.class))
            })
    @GetMapping(value = "estados", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<StateResponse>> findAllStatesByActive(@RequestParam(required = false, defaultValue = "true", name = "ativo") final boolean active);
}
