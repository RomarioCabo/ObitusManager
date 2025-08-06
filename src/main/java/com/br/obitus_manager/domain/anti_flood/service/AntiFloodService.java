package com.br.obitus_manager.domain.anti_flood.service;

public interface AntiFloodService {
    void validateAttempt(String email);
}
