package com.br.obitus_manager.infrastructure.persistence.state;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface StateRepository extends JpaRepository<StateEntity, UUID> {

    @Query("SELECT state FROM StateEntity state WHERE state.id = :idState")
    Optional<StateEntity> findById(UUID idState);

    @Query("SELECT state FROM StateEntity state WHERE state.active = :active")
    List<StateEntity> findAllByActive(boolean active);
}
