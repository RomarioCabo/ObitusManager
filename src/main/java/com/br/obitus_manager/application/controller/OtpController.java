package com.br.obitus_manager.application.controller;

import com.br.obitus_manager.application.exception.ErrorHttpResponseDto;
import com.br.obitus_manager.domain.otp.OtpCreateRequest;
import com.br.obitus_manager.domain.otp.OtpResponse;
import com.br.obitus_manager.domain.otp.OtpValidateRequest;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tags(@Tag(name = "OTP", description = "OTP REST Controller"))
public interface OtpController {

    @ApiResponse(
            responseCode = "201",
            description = "OTP criado com sucesso.",
            content = {@Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = OtpResponse.class))
            })
    @ApiResponse(
            responseCode = "400",
            description = "Bad request.",
            content = {@Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorHttpResponseDto.class))
            })
    @PostMapping(
            value = "otp/generate",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    ResponseEntity<OtpResponse> generate(@RequestBody @Valid OtpCreateRequest request);

    @ApiResponse(
            responseCode = "200",
            description = "OTP validado com sucesso.",
            content = {@Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema())
            })
    @ApiResponse(
            responseCode = "400",
            description = "Bad request.",
            content = {@Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorHttpResponseDto.class))
            })
    @PostMapping(
            value = "otp/validate",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    ResponseEntity<Void> validate(@RequestBody @Valid OtpValidateRequest request);
}
