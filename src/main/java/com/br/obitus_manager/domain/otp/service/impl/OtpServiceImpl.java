package com.br.obitus_manager.domain.otp.service.impl;

import com.br.obitus_manager.application.exception.OTPAlreadySentException;
import com.br.obitus_manager.domain.DatabaseProvider;
import com.br.obitus_manager.domain.otp.OtpCreateRequest;
import com.br.obitus_manager.domain.otp.OtpDto;
import com.br.obitus_manager.domain.otp.OtpResponse;
import com.br.obitus_manager.domain.otp.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final DatabaseProvider databaseProvider;

    @Override
    public OtpResponse generateOtp(OtpCreateRequest request) {
        Optional<OtpDto> otp = databaseProvider.findTopByEmailOrderByCreatedAtDesc(request.getEmail());
        if (otp.isPresent() && otp.get().isValid()) {
            throw new OTPAlreadySentException("OTP Already Sent");
        }

        String code = generateCode();
        String hash = DigestUtils.sha256Hex(code);

        databaseProvider.saveOtp(request.getEmail(), hash);
        return OtpResponse.builder().pin(code).build();
    }

    private String generateCode() {
        return String.valueOf(new Random().nextInt(900_000) + 100_000);
    }
}
