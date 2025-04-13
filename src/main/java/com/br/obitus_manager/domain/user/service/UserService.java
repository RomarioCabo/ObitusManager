package com.br.obitus_manager.domain.user.service;

import com.br.obitus_manager.domain.user.UserRequest;
import com.br.obitus_manager.domain.user.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse upsert(final UUID currentSessionUserId, final UserRequest request, final UUID userId);

    UserResponse findByEmail(final String email);

    List<UserResponse> findAll();

    UserResponse findById(final UUID currentSessionUserId, final UUID userId);
}
