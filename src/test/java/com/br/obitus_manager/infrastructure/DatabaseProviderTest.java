package com.br.obitus_manager.infrastructure;

import com.br.obitus_manager.domain.state.StateRequest;
import com.br.obitus_manager.domain.state.StateResponse;
import com.br.obitus_manager.domain.user.UserRequest;
import com.br.obitus_manager.domain.user.UserResponse;
import com.br.obitus_manager.infrastructure.persistence.DatabaseProviderImpl;
import com.br.obitus_manager.infrastructure.persistence.city.CityEntity;
import com.br.obitus_manager.infrastructure.persistence.custom.CustomRepository;
import com.br.obitus_manager.infrastructure.persistence.state.StateEntity;
import com.br.obitus_manager.infrastructure.persistence.state.StateRepository;
import com.br.obitus_manager.infrastructure.persistence.user.UserEntity;
import com.br.obitus_manager.infrastructure.persistence.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class DatabaseProviderTest {

    @InjectMocks
    private DatabaseProviderImpl databaseProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StateRepository stateRepository;

    @Mock
    private CustomRepository customRepository;

    private final UUID USER_ID = UUID.randomUUID();
    private final String EMAIL = "maria@exemplo.com";
    private final String USER_NAME = "Maria";
    private final String ROLE = "ROLE_ADMIN";

    private final UUID STATE_ID = UUID.randomUUID();

    private final UUID CITY_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(databaseProvider, "baseUrl", "http://localhost:8080");
    }

    @Test
    void shouldSaveUser() {
        UserRequest request = buildUserRequest();

        Mockito.when(userRepository.saveAndFlush(Mockito.any(UserEntity.class))).thenReturn(buildUserEntity());

        UserResponse result = databaseProvider.saveUser(buildUserRequest(), USER_ID);

        assertNotNull(result);
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getEmail(), result.getEmail());
        assertEquals(ROLE, result.getRole());
        assertEquals(USER_ID, result.getId());

        Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(Mockito.any(UserEntity.class));
    }

    @Test
    void shouldReturnUserByEmail() {
        Mockito.when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(buildUserEntity());

        UserResponse result = databaseProvider.findUserByEmail(EMAIL);

        assertNotNull(result);
        assertEquals(USER_NAME, result.getName());
        assertEquals(EMAIL, result.getEmail());
        assertEquals(ROLE, result.getRole());
        assertEquals(USER_ID, result.getId());

        Mockito.verify(userRepository, Mockito.times(1)).findUserByEmail(Mockito.anyString());
    }

    @Test
    void shouldReturnUserNull() {
        Mockito.when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(null);

        UserResponse result = databaseProvider.findUserByEmail(EMAIL);

        assertNull(result);

        Mockito.verify(userRepository, Mockito.times(1)).findUserByEmail(Mockito.anyString());
    }

    @Test
    void shouldReturnUserById() {
        Mockito.when(userRepository.findUserById(Mockito.any(UUID.class))).thenReturn(buildUserEntity());

        UserResponse result = databaseProvider.findUserById(USER_ID);

        assertNotNull(result);
        assertEquals(USER_NAME, result.getName());
        assertEquals(EMAIL, result.getEmail());
        assertEquals(ROLE, result.getRole());
        assertEquals(USER_ID, result.getId());

        Mockito.verify(userRepository, Mockito.times(1)).findUserById(Mockito.any(UUID.class));
    }

    @Test
    void shouldReturnAllUsers() {
        Mockito.when(userRepository.findAllUsers()).thenReturn(Collections.singletonList(buildUserEntity()));

        List<UserResponse> result = databaseProvider.findAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        Mockito.verify(userRepository, Mockito.times(1)).findAllUsers();
    }

    @Test
    void shouldReturnNullForAllUsers() {
        Mockito.when(userRepository.findAllUsers()).thenReturn(null);

        List<UserResponse> result = databaseProvider.findAllUsers();

        assertNotNull(result);
        assertEquals(0, result.size());
        Mockito.verify(userRepository, Mockito.times(1)).findAllUsers();
    }

    @Test
    void shouldSaveAllStates() {
        Mockito.when(stateRepository.findAllByIds(Mockito.anyList()))
                .thenReturn(Collections.singletonList(buildStateEntity()));
        Mockito.when(stateRepository.saveAll(Mockito.anyList()))
                .thenReturn(Collections.singletonList(buildStateEntity()));

        List<StateResponse> result = databaseProvider.saveAllStates(Collections.singletonList(buildStateRequest()));

        assertNotNull(result);
        assertEquals(1, result.size());

        Mockito.verify(stateRepository, Mockito.times(1)).findAllByIds(Mockito.anyList());
        Mockito.verify(stateRepository, Mockito.times(1)).saveAll(Mockito.anyList());
    }

    @Test
    void shouldReturnAllStates() {
        Mockito.when(customRepository.findWithFilters(eq(StateEntity.class), Mockito.anyMap(), Mockito.anyMap(),
                        Mockito.any(Pageable.class), Mockito.anyString()))
                .thenReturn(new PageImpl<>(List.of(buildStateEntity())));

        List<StateResponse> result = databaseProvider.findAllStates(new HashMap<>(), new HashMap<>(),
                PageRequest.of(0, 10), "name");

        assertNotNull(result);
        assertEquals(1, result.size());

        Mockito.verify(customRepository, Mockito.times(1))
                .findWithFilters(eq(StateEntity.class), Mockito.anyMap(), Mockito.anyMap(), Mockito.any(Pageable.class),
                        Mockito.anyString());
    }

    private UserEntity buildUserEntity() {
        return new UserEntity(USER_ID, buildUserRequest());
    }

    private UserRequest buildUserRequest() {
        return UserRequest.builder()
                .name(USER_NAME)
                .email(EMAIL)
                .password("ANY_PASSWORD")
                .build();
    }

    private StateEntity buildStateEntity() {
        return StateEntity.builder()
                .id(STATE_ID)
                .name("anyState")
                .acronym("ANY_ACRONYM")
                .active(true)
                .build();
    }

    private StateRequest buildStateRequest() {
        return StateRequest.builder()
                .id(STATE_ID)
                .active(true)
                .build();
    }

    private CityEntity buildCityEntity() {
        return CityEntity.builder()
                .id(CITY_ID)
                .stateEntity(buildStateEntity())
                .name("ANY_CITY")
                .build();
    }
}
