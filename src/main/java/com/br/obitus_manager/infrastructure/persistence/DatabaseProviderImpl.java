package com.br.obitus_manager.infrastructure.persistence;

import com.br.obitus_manager.domain.DatabaseProvider;
import com.br.obitus_manager.domain.city.CityRequest;
import com.br.obitus_manager.domain.city.CityResponse;
import com.br.obitus_manager.domain.state.StateResponse;
import com.br.obitus_manager.domain.user.UserRequest;
import com.br.obitus_manager.domain.user.UserResponse;
import com.br.obitus_manager.infrastructure.persistence.city.CityEntity;
import com.br.obitus_manager.infrastructure.persistence.city.CityRepository;
import com.br.obitus_manager.infrastructure.persistence.state.StateEntity;
import com.br.obitus_manager.infrastructure.persistence.state.StateRepository;
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
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;

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

    @Override
    public List<StateResponse> findAllStatesByActive(final boolean active) {
        return Optional.ofNullable(stateRepository.findAllByActive(active))
                .orElse(Collections.emptyList())
                .stream()
                .map(StateEntity::toModel)
                .toList();
    }

    @Override
    @Transactional
    public CityResponse saveCity(final CityRequest request, final UUID cityId) {
        return stateRepository.findById(request.getIdState())
                .map(state -> {
                    CityEntity city = new CityEntity(cityId, state, request);
                    return cityRepository.saveAndFlush(city).toModel();
                })
                .orElse(null);
    }

    @Override
    public boolean existsCityByNameAndState(String name, UUID stateId) {
        return cityRepository.existsByNameAndState(name, stateId);
    }

    @Override
    public List<CityResponse> findAllCitiesByState(final UUID stateId) {
        return Optional.ofNullable(cityRepository.findAllByState(stateId))
                .orElse(Collections.emptyList())
                .stream()
                .map(CityEntity::toModel)
                .toList();
    }
}
