package com.br.obitus_manager.application.controller.impl;

import com.br.obitus_manager.application.controller.OtpController;
import com.br.obitus_manager.application.util.ControllerUtils;
import com.br.obitus_manager.domain.message.MessageRequest;
import com.br.obitus_manager.domain.messaging.MessagingService;
import com.br.obitus_manager.domain.otp.OtpCreateRequest;
import com.br.obitus_manager.domain.otp.OtpResponse;
import com.br.obitus_manager.domain.otp.OtpValidateRequest;
import com.br.obitus_manager.domain.otp.service.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OtpControllerImpl extends ControllerUtils implements OtpController {

    private final OtpService otpService;
    private final MessagingService messagingService;

    @Override
    public ResponseEntity<Void> generate(OtpCreateRequest request) {
        OtpResponse response = otpService.generateOtp(request);
        messagingService.sendEmail(buildMessageRequest(response.getPin(), request.getEmail()));

        return ResponseEntity.created(null).build();
    }

    @Override
    public ResponseEntity<Void> validate(OtpValidateRequest request) {
        otpService.validateOtp(request);
        return ResponseEntity.noContent().build();
    }

    private MessageRequest buildMessageRequest(final String pin, final String email) {
        return MessageRequest.builder()
                .templateId("otp-template")
                .templateVariables(Map.of("otpCode", pin))
                .emailTo(email)
                .subject("Rquisição do OTP")
                .build();
    }
}
