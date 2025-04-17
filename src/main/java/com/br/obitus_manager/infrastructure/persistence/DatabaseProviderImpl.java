package com.br.obitus_manager.infrastructure.persistence;

import com.br.obitus_manager.domain.DatabaseProvider;
import com.br.obitus_manager.domain.city.CityRequest;
import com.br.obitus_manager.domain.city.CityResponse;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeRequest;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeResponse;
import com.br.obitus_manager.domain.state.StateResponse;
import com.br.obitus_manager.domain.user.UserRequest;
import com.br.obitus_manager.domain.user.UserResponse;
import com.br.obitus_manager.infrastructure.persistence.city.CityEntity;
import com.br.obitus_manager.infrastructure.persistence.city.CityRepository;
import com.br.obitus_manager.infrastructure.persistence.custom.CustomRepository;
import com.br.obitus_manager.infrastructure.persistence.obituary_notice.ObituaryNoticeEntity;
import com.br.obitus_manager.infrastructure.persistence.obituary_notice.ObituaryNoticeRepository;
import com.br.obitus_manager.infrastructure.persistence.state.StateEntity;
import com.br.obitus_manager.infrastructure.persistence.state.StateRepository;
import com.br.obitus_manager.infrastructure.persistence.user.UserEntity;
import com.br.obitus_manager.infrastructure.persistence.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@RequiredArgsConstructor
public class DatabaseProviderImpl implements DatabaseProvider {

    @Value("${base_url}")
    private String baseUrl;

    private final UserRepository userRepository;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
    private final ObituaryNoticeRepository obituaryNoticeRepository;
    private final CustomRepository customRepository;

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
    public List<StateResponse> findAllStates(final Map<String, Object> filters,
                                             final Map<String, Map<String, Object>> advancedFilters,
                                             final Pageable pageable) {
        final Page<StateEntity> stateEntities
                = customRepository.findWithFilters(StateEntity.class, filters, advancedFilters, pageable);

        return Optional.ofNullable(stateEntities)
                .map(page -> page.getContent()
                        .stream()
                        .map(StateEntity::toModel)
                        .toList())
                .orElse(null);
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

    @Override
    @Transactional
    public ObituaryNoticeResponse saveObituaryNotice(final ObituaryNoticeRequest request, final UUID obituaryNoticeId) {
        return cityRepository.findById(request.getIdCity())
                .map(cityEntity -> {
                    ObituaryNoticeEntity obituaryNoticeEntity = new ObituaryNoticeEntity(obituaryNoticeId, cityEntity, request);
                    return obituaryNoticeRepository.saveAndFlush(obituaryNoticeEntity).toModel(baseUrl);
                })
                .orElse(null);
    }

    @Override
    public byte[] getPhotoByIdObituaryNoticeId(final UUID obituaryNoticeId) {
        return obituaryNoticeRepository.findById(obituaryNoticeId).map(ObituaryNoticeEntity::getPhoto).orElse(null);
    }

    @Override
    public List<ObituaryNoticeResponse> findObituaryNotice(final Map<String, Object> filters,
                                                           final Map<String, Map<String, Object>> advancedFilters,
                                                           final Pageable pageable) {
        final Page<ObituaryNoticeEntity> obituaryNoticeEntities
                = customRepository.findWithFilters(ObituaryNoticeEntity.class, filters, advancedFilters, pageable);

        return Optional.ofNullable(obituaryNoticeEntities)
                .map(page -> page.getContent()
                        .stream()
                        .map(entity -> entity.toModel(baseUrl))
                        .toList())
                .orElse(null);
    }
}
