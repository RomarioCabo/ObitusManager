package com.br.obitus_manager.infrastructure.persistence.otp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface OtpRepository extends JpaRepository<OtpEntity, UUID> {
    Optional<OtpEntity> findTopByEmailOrderByCreatedAtDesc(String email);
    Optional<OtpEntity> findByEmailAndCodeHash(String email, String codeHash);
}
