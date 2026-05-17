package com.br.obitus_manager.application.controller;

import com.br.obitus_manager.application.exception.ErrorHttpResponseDto;
import com.br.obitus_manager.domain.otp.OtpCreateRequest;
import com.br.obitus_manager.domain.otp.OtpValidateRequest;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "OTP", description = "OTP REST Controller")
public interface OtpController {

    @ApiResponse(
            responseCode = "201",
            description = "OTP criado com sucesso."
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
            value = "/otp/generate",
            consumes = APPLICATION_JSON_VALUE
    )
    ResponseEntity<Void> generate(
            @Valid @RequestBody OtpCreateRequest request
    );

    @ApiResponse(
            responseCode = "200",
            description = "OTP validado com sucesso."
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
            value = "/otp/validate",
            consumes = APPLICATION_JSON_VALUE
    )
    ResponseEntity<Void> validate(
            @Valid @RequestBody OtpValidateRequest request
    );
}