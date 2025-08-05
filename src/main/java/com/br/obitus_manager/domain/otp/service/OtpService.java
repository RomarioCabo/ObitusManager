package com.br.obitus_manager.domain.otp.service;

import com.br.obitus_manager.domain.otp.OtpCreateRequest;
import com.br.obitus_manager.domain.otp.OtpResponse;

public interface OtpService {
    OtpResponse generateOtp(OtpCreateRequest request);
}
