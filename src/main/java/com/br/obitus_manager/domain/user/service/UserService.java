package com.br.obitus_manager.domain.user.service;

import com.br.obitus_manager.domain.common.PageResponse;
import com.br.obitus_manager.domain.user.UserRequest;
import com.br.obitus_manager.domain.user.UserResponse;

import java.util.UUID;

public interface UserService {
    UserResponse upsert(final UUID currentSessionUserId, final UserRequest request, final UUID userId);

    UserResponse findByEmail(final String email);

    PageResponse<UserResponse> findAll(final Integer page, final Integer size, final String sort);

    UserResponse findById(final UUID currentSessionUserId, final UUID userId);
}
