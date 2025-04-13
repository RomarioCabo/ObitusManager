package com.br.obitus_manager.infrastructure.persistence;

import com.br.obitus_manager.domain.DatabaseProvider;
import com.br.obitus_manager.domain.user.UserRequest;
import com.br.obitus_manager.domain.user.UserResponse;
import com.br.obitus_manager.infrastructure.persistence.user.UserEntity;
import com.br.obitus_manager.infrastructure.persistence.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DatabaseProviderImpl implements DatabaseProvider {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponse saveUser(final UserRequest request, final UUID userId) {
        final UserEntity userEntity = new UserEntity(userId, request);
        final UserEntity createdUser = userRepository.saveAndFlush(userEntity);

        return createdUser.toModel();
    }

    @Override
    public UserResponse findUserByEmail(final String email) {
        return Optional.ofNullable(userRepository.findUserByEmail(email))
                .map(UserEntity::toModel)
                .orElse(null);
    }

    @Override
    public UserResponse findUserById(final UUID userId) {
        return Optional.ofNullable(userRepository.findUserById(userId))
                .map(UserEntity::toModel)
                .orElse(null);
    }

    @Override
    public List<UserResponse> findAllUsers() {
        return Optional.ofNullable(userRepository.findAllUsers())
                .orElse(Collections.emptyList())
                .stream()
                .map(UserEntity::toModel)
                .toList();
    }
}
