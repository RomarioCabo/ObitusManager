package com.br.obitus_manager.domain.user.service.impl;

import com.br.obitus_manager.application.exception.BadRequestException;
import com.br.obitus_manager.domain.DatabaseProvider;
import com.br.obitus_manager.domain.user.UserRequest;
import com.br.obitus_manager.domain.user.UserResponse;
import com.br.obitus_manager.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final DatabaseProvider databaseProvider;
    private final PasswordEncoder passwordEncoder;

    private final String specialCharacters = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/`~";

    @Override
    public UserResponse upsert(final UUID currentSessionUserId, final UserRequest request, final UUID userId) {
        if (userId == null) {
            verifyRequiredFields(request);
        }

        if (userId == null && request.getPassword() != null) {
            validateComplexityPassword(request.getPassword());
        }

        validateUserExistenceByEmail(request.getEmail());

        UserResponse userResponseFromDb = findById(currentSessionUserId, userId);

        request.setName(Optional.ofNullable(request.getName()).orElse(userResponseFromDb != null ? userResponseFromDb.getName() : null));
        request.setEmail(Optional.ofNullable(request.getEmail()).orElse(userResponseFromDb != null ? userResponseFromDb.getEmail() : null));
        request.setPassword(Optional.ofNullable(request.getPassword()).map(passwordEncoder::encode).orElse(userResponseFromDb != null ? userResponseFromDb.getPassword() : null));

        return databaseProvider.saveUser(request, userId);
    }

    @Override
    public List<UserResponse> findAll() {
        return databaseProvider.findAllUsers();
    }

    @Override
    public UserResponse findById(final UUID currentSessionUserId, final UUID userId) {
        if (userId == null) {
            return null;
        }

        return databaseProvider.findUserById(userId);
    }

    @Override
    public UserResponse findByEmail(final String email) {
        return databaseProvider.findUserByEmail(email);
    }

    private void validateUserExistenceByEmail(final String email) {
        Optional.ofNullable(findByEmail(email)).ifPresent(user -> {
            throw new BadRequestException(String.format("Usuário com o e-mail: %s já existe!", user.getEmail()));
        });
    }

    private void validateComplexityPassword(final String password) {
        List<String> errors = Stream.of(
                        password.length() < 8 ? "A senha deve conter no mínimo 8 caracteres!" : null,
                        password.chars().noneMatch(c -> specialCharacters.indexOf(c) >= 0) ? "A senha deve conter no mínimo um caracter especial!" : null,
                        password.chars().noneMatch(Character::isUpperCase) ? "A senha deve conter no mínimo uma letra maiúscula!" : null
                ).filter(Objects::nonNull)
                .toList();

        if (!errors.isEmpty()) {
            throw new BadRequestException(String.join("\n", errors));
        }
    }

    private void verifyRequiredFields(final UserRequest request) {
        List<String> errors = Stream.of(
                        isNullOrEmpty(request.getEmail()) ? "O campo email é obrigatório." : null,
                        isNullOrEmpty(request.getPassword()) ? "O campo senha é obrigatório." : null
                ).filter(Objects::nonNull)
                .toList();

        if (!errors.isEmpty()) {
            throw new BadRequestException(String.join("\n", errors));
        }
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isBlank();
    }
}
