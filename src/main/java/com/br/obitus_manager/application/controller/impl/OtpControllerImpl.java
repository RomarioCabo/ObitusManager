package com.br.obitus_manager.application.controller.impl;

import com.br.obitus_manager.application.controller.OtpController;
import com.br.obitus_manager.application.util.ControllerUtils;
import com.br.obitus_manager.domain.otp.OtpCreateRequest;
import com.br.obitus_manager.domain.otp.OtpResponse;
import com.br.obitus_manager.domain.otp.service.OtpService;
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
public class OtpControllerImpl extends ControllerUtils implements OtpController {

    private final OtpService otpService;

    @Override
    public ResponseEntity<OtpResponse> otp(OtpCreateRequest request) {
        OtpResponse response = otpService.generateOtp(request);
        return ResponseEntity.created(null).body(response);
    }
}
