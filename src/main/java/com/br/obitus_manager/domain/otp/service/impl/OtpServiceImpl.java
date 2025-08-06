package com.br.obitus_manager.domain.otp.service.impl;

import com.br.obitus_manager.application.exception.BadRequestException;
import com.br.obitus_manager.domain.DatabaseProvider;
import com.br.obitus_manager.domain.anti_flood.service.AntiFloodService;
import com.br.obitus_manager.domain.otp.OtpCreateRequest;
import com.br.obitus_manager.domain.otp.OtpDto;
import com.br.obitus_manager.domain.otp.OtpResponse;
import com.br.obitus_manager.domain.otp.OtpValidateRequest;
import com.br.obitus_manager.domain.otp.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final DatabaseProvider databaseProvider;

    private final AntiFloodService antiFloodService;

    @Override
    public OtpResponse generateOtp(OtpCreateRequest request) {
        antiFloodService.validateAttempt(request.getEmail());

        Optional<OtpDto> otp = databaseProvider.findTopByEmailOrderByCreatedAtDesc(request.getEmail());
        if (otp.isPresent() && otp.get().isValid()) {
            throw new BadRequestException("OTP Already Sent");
        }

        String code = generateCode();
        String hash = DigestUtils.sha256Hex(code);

        databaseProvider.saveOtp(null, request.getEmail(), hash, LocalDateTime.now(), false);
        return OtpResponse.builder().pin(code).build();
    }

    @Override
    public void validateOtp(OtpValidateRequest request) {
        antiFloodService.validateAttempt(request.getEmail());

        String hash = DigestUtils.sha256Hex(request.getCode());
        OtpDto otp = databaseProvider.findByEmailAndCodeHash(request.getEmail(), hash)
                .orElseThrow(() -> new BadRequestException("Código inválido ou inexistente."));

        if (otp.isExpired()) throw new BadRequestException("Código expirado.");
        if (otp.isUsed()) throw new BadRequestException("Código já utilizado.");

        if (otp.getCodeHash().equals(hash)) {
            otp.setUsed(true);
        }

        databaseProvider.saveOtp(otp.getId(), otp.getEmail(), otp.getCodeHash(), otp.getCreatedAt(), otp.isUsed());

        if (!otp.isUsed()) {
            throw new BadRequestException("Código inválido.");
        }
    }

    private String generateCode() {
        return String.valueOf(new Random().nextInt(900_000) + 100_000);
    }
}
