package com.br.obitus_manager.domain.otp.service;

import com.br.obitus_manager.domain.otp.OtpCreateRequest;
import com.br.obitus_manager.domain.otp.OtpResponse;
import com.br.obitus_manager.domain.otp.OtpValidateRequest;

public interface OtpService {
    OtpResponse generateOtp(OtpCreateRequest request);

    void validateOtp(OtpValidateRequest request);
}
