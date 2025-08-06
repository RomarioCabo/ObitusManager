package com.br.obitus_manager.infrastructure.persistence;

import com.br.obitus_manager.domain.DatabaseProvider;
import com.br.obitus_manager.domain.anti_flood.AntiFloodDto;
import com.br.obitus_manager.domain.city.CityRequest;
import com.br.obitus_manager.domain.city.CityResponse;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeRequest;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeResponse;
import com.br.obitus_manager.domain.otp.OtpDto;
import com.br.obitus_manager.domain.state.StateRequest;
import com.br.obitus_manager.domain.state.StateResponse;
import com.br.obitus_manager.domain.user.UserRequest;
import com.br.obitus_manager.domain.user.UserResponse;
import com.br.obitus_manager.infrastructure.persistence.anti_flood.AntiFloodEntity;
import com.br.obitus_manager.infrastructure.persistence.anti_flood.AntiFloodRepository;
import com.br.obitus_manager.infrastructure.persistence.city.CityEntity;
import com.br.obitus_manager.infrastructure.persistence.city.CityRepository;
import com.br.obitus_manager.infrastructure.persistence.custom.CustomRepository;
import com.br.obitus_manager.infrastructure.persistence.obituary_notice.ObituaryNoticeEntity;
import com.br.obitus_manager.infrastructure.persistence.obituary_notice.ObituaryNoticeRepository;
import com.br.obitus_manager.infrastructure.persistence.otp.OtpEntity;
import com.br.obitus_manager.infrastructure.persistence.otp.OtpRepository;
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

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private final OtpRepository otpRepository;
    private final AntiFloodRepository antiFloodRepository;

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
    @Transactional
    public List<StateResponse> saveAllStates(final List<StateRequest> stateRequests) {
        Map<UUID, Boolean> activeById = stateRequests.stream()
                .collect(Collectors.toMap(StateRequest::getId, StateRequest::getActive));

        List<StateEntity> updatedStates = stateRepository.findAllByIds(new ArrayList<>(activeById.keySet()))
                .stream()
                .peek(state -> {
                    Boolean newActive = activeById.get(state.getId());
                    if (newActive != null) state.setActive(newActive);
                })
                .collect(Collectors.toList());

        return stateRepository.saveAll(updatedStates).stream()
                .map(StateEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<StateResponse> findAllStates(final Map<String, Object> filters,
                                             final Map<String, Map<String, Object>> advancedFilters,
                                             final Pageable pageable, final String nameForOrderBy) {
        final Page<StateEntity> stateEntities
                = customRepository.findWithFilters(StateEntity.class, filters, advancedFilters, pageable, nameForOrderBy);

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
    public List<CityResponse> findAllCities(final Map<String, Object> filters,
                                            final Map<String, Map<String, Object>> advancedFilters,
                                            final Pageable pageable, final String nameForOrderBy) {
        final Page<CityEntity> cityEntities
                = customRepository.findWithFilters(CityEntity.class, filters, advancedFilters, pageable, nameForOrderBy);

        return Optional.ofNullable(cityEntities)
                .map(page -> page.getContent()
                        .stream()
                        .map(CityEntity::toModel)
                        .toList())
                .orElse(null);
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
                                                           final Pageable pageable, final String nameForOrderBy) {
        final Page<ObituaryNoticeEntity> obituaryNoticeEntities
                = customRepository.findWithFilters(ObituaryNoticeEntity.class, filters, advancedFilters, pageable, nameForOrderBy);

        return Optional.ofNullable(obituaryNoticeEntities)
                .map(page -> page.getContent()
                        .stream()
                        .map(entity -> entity.toModel(baseUrl))
                        .toList())
                .orElse(null);
    }

    @Override
    @Transactional
    public void saveOtp(final UUID idOtp, final String email, final String hash, final LocalDateTime createdAt,
                        final boolean used) {
        OtpEntity otpEntity = OtpEntity.builder()
                .id(idOtp)
                .email(email)
                .codeHash(hash)
                .createdAt(createdAt)
                .used(used)
                .build();

        otpRepository.saveAndFlush(otpEntity);
    }

    @Override
    public Optional<OtpDto> findTopByEmailOrderByCreatedAtDesc(final String email) {
        return otpRepository.findTopByEmailOrderByCreatedAtDesc(email)
                .map(OtpEntity::toDto);
    }

    @Override
    public Optional<OtpDto> findByEmailAndCodeHash(final String email, final String codeHash) {
        return otpRepository.findByEmailAndCodeHash(email, codeHash)
                .map(OtpEntity::toDto);
    }

    @Override
    public void saveAntiFlood(final UUID id, final String email, final int attempts, final LocalDateTime blockStart,
                              final LocalDateTime blockEnd, int blockedLevel) {
        AntiFloodEntity antiFloodEntity = AntiFloodEntity.builder()
                .id(id)
                .email(email)
                .attempts(attempts)
                .blockStart(blockStart)
                .blockEnd(blockEnd)
                .blockedLevel(blockedLevel)
                .build();

        antiFloodRepository.saveAndFlush(antiFloodEntity);
    }

    @Override
    public AntiFloodDto findByEmail(final String email) {
        return antiFloodRepository.findByEmail(email)
                .map(AntiFloodEntity::toDto)
                .orElse(null);
    }
}
