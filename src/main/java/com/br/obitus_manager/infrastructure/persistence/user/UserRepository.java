package com.br.obitus_manager.infrastructure.persistence.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Query("SELECT ue FROM UserEntity ue WHERE ue.id = :userId")
    UserEntity findUserById(UUID userId);

    @Query("SELECT ue FROM UserEntity ue WHERE ue.email = :email")
    UserEntity findUserByEmail(String email);

    @Query("SELECT ue FROM UserEntity ue")
    List<UserEntity> findAllUsers();
}
