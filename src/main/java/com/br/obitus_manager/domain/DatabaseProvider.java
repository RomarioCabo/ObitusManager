package com.br.obitus_manager.domain;

import com.br.obitus_manager.domain.city.CityRequest;
import com.br.obitus_manager.domain.city.CityResponse;
import com.br.obitus_manager.domain.state.StateResponse;
import com.br.obitus_manager.domain.user.UserRequest;
import com.br.obitus_manager.domain.user.UserResponse;

import java.util.List;
import java.util.UUID;

public interface DatabaseProvider {

    UserResponse saveUser(final UserRequest request, final UUID userId);

    UserResponse findUserByEmail(final String email);

    UserResponse findUserById(final UUID userId);

    List<UserResponse> findAllUsers();

    List<StateResponse> findAllStatesByActive(final boolean active);

    CityResponse saveCity(final CityRequest request, final UUID cityId);

    boolean existsCityByNameAndState(String name, UUID stateId);

    List<CityResponse> findAllCitiesByState(final UUID stateId);
}
