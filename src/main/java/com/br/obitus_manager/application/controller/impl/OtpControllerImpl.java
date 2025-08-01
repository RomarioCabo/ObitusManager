package com.br.obitus_manager.application.controller.impl;

import com.br.obitus_manager.application.controller.OtpController;
import com.br.obitus_manager.domain.otp.OtpCreateRequest;
import com.br.obitus_manager.domain.otp.OtpRequest;
import com.br.obitus_manager.domain.otp.OtpValidateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OtpControllerImpl implements OtpController {

    @Override
    public ResponseEntity<Void> otp(OtpRequest request) {
        switch (request.getType()) {
            case CREATE:
                OtpCreateRequest createRequest = (OtpCreateRequest) request;
                log.info("Create Otp Request {}", createRequest);
                return ResponseEntity.ok().build();

            case VALIDATE:
                OtpValidateRequest validateRequest = (OtpValidateRequest) request;
                log.info("Validating Otp Request {}", validateRequest);
                return ResponseEntity.ok().build();

            default:
                return ResponseEntity.badRequest().build();
        }
    }
}
