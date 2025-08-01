package com.br.obitus_manager.application.controller;

import com.br.obitus_manager.domain.otp.OtpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tags(@Tag(name = "OTP", description = "OTP REST Controller"))
public interface OtpController {

    @Operation(
            summary = "Cria ou valida um OTP",
            description = """
        Este endpoint aceita uma requisição polimórfica baseada no campo `type`.

        - `type = CREATE:` Criação de um OTP. Corpo esperado:
        ```json
        {
          "type": "CREATE",
          "templateId": "string",
          "email": "string"
        }
        ```
        - `type = VALIDATE:` Validação de um OTP. Corpo esperado:
        ```json
        {
          "type": "VALIDATE",
          "templateId": "string",
          "email": "string",
          "pin": 123456,
          "hash": "uuid"
        }
        ```
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requisição processada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou tipo inválido", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @PostMapping(value = "data/otp", consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<Void> otp(@Valid @RequestBody OtpRequest request);
}
