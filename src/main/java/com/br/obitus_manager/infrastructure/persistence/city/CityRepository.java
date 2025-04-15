package com.br.obitus_manager.infrastructure.persistence.city;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface CityRepository extends JpaRepository<CityEntity, UUID> {

    @Query("SELECT c FROM CityEntity c WHERE c.id = :idCity")
    Optional<CityEntity> findById(final UUID idCity);

    @Query("SELECT COUNT(c) > 0 FROM CityEntity c WHERE c.name = :name AND c.stateEntity.id = :stateId")
    boolean existsByNameAndState(final String name, final UUID stateId);

    @Query("SELECT c FROM CityEntity c WHERE c.stateEntity.id = :stateId")
    List<CityEntity> findAllByState(final UUID stateId);
}
