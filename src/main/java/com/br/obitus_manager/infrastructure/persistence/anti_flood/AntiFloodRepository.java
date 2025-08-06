package com.br.obitus_manager.infrastructure.persistence.anti_flood;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface AntiFloodRepository extends JpaRepository<AntiFloodEntity, UUID> {
    Optional<AntiFloodEntity> findByEmail(String email);
}
