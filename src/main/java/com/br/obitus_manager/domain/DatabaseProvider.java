package com.br.obitus_manager.domain;

import com.br.obitus_manager.domain.user.UserRequest;
import com.br.obitus_manager.domain.user.UserResponse;

import java.util.List;
import java.util.UUID;

public interface DatabaseProvider {

    UserResponse saveUser(final UserRequest request, final UUID userId);

    UserResponse findUserByEmail(final String email);

    UserResponse findUserById(final UUID userId);

    List<UserResponse> findAllUsers();
}
