package com.br.obitus_manager.infrastructure.persistence.obituary_notice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ObituaryNoticeRepository extends JpaRepository<ObituaryNoticeEntity, UUID> {

    @Query("SELECT one FROM ObituaryNoticeEntity one WHERE one.id = :obituaryNoticeId")
    Optional<ObituaryNoticeEntity> findById(final UUID obituaryNoticeId);
}
