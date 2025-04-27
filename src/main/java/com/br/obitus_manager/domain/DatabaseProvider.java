package com.br.obitus_manager.domain;

import com.br.obitus_manager.domain.city.CityRequest;
import com.br.obitus_manager.domain.city.CityResponse;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeRequest;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeResponse;
import com.br.obitus_manager.domain.state.StateRequest;
import com.br.obitus_manager.domain.state.StateResponse;
import com.br.obitus_manager.domain.user.UserRequest;
import com.br.obitus_manager.domain.user.UserResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface DatabaseProvider {

    UserResponse saveUser(final UserRequest request, final UUID userId);

    UserResponse findUserByEmail(final String email);

    UserResponse findUserById(final UUID userId);

    List<UserResponse> findAllUsers();

    List<StateResponse> saveAllStates(final List<StateRequest> stateRequests);

    List<StateResponse> findAllStates(final Map<String, Object> filters,
                                      final Map<String, Map<String, Object>> advancedFilters,
                                      final Pageable pageable, final String nameForOrderBy);

    CityResponse saveCity(final CityRequest request, final UUID cityId);

    boolean existsCityByNameAndState(String name, UUID stateId);

    List<CityResponse> findAllCities(final Map<String, Object> filters,
                                     final Map<String, Map<String, Object>> advancedFilters,
                                     final Pageable pageable, final String nameForOrderBy);

    ObituaryNoticeResponse saveObituaryNotice(final ObituaryNoticeRequest request, final UUID obituaryNoticeId);

    byte[] getPhotoByIdObituaryNoticeId(final UUID obituaryNoticeId);

    List<ObituaryNoticeResponse> findObituaryNotice(final Map<String, Object> filters,
                                                    final Map<String, Map<String, Object>> advancedFilters,
                                                    final Pageable pageable, final String nameForOrderBy);
}
