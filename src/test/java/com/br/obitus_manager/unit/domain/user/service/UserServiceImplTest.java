package com.br.obitus_manager.unit.domain.user.service;

import com.br.obitus_manager.application.exception.BadRequestException;
import com.br.obitus_manager.domain.DatabaseProvider;
import com.br.obitus_manager.domain.user.UserRequest;
import com.br.obitus_manager.domain.user.UserResponse;
import com.br.obitus_manager.domain.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private DatabaseProvider databaseProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void createRejectsMissingMandatoryFields() {
        UserRequest request = UserRequest.builder().name("Nome").build();

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> userService.upsert(null, request, null)
        );

        assertTrue(ex.getMessage().contains("email"));
        assertTrue(ex.getMessage().contains("senha"));
        verify(databaseProvider, never()).saveUser(any(), any());
    }

    @Test
    void createRejectsWeakPassword() {
        UserRequest request = UserRequest.builder()
                .name("Nome")
                .email("user@test.com")
                .password("fraca")
                .build();

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> userService.upsert(null, request, null)
        );

        assertTrue(ex.getMessage().contains("8 caracteres"));
        verify(databaseProvider, never()).saveUser(any(), any());
    }

    @Test
    void createRejectsDuplicateEmail() {
        UserRequest request = UserRequest.builder()
                .name("Nome")
                .email("dup@test.com")
                .password("SenhaForte@1")
                .build();

        when(databaseProvider.findUserByEmail("dup@test.com"))
                .thenReturn(UserResponse.builder().email("dup@test.com").build());

        assertThrows(BadRequestException.class, () -> userService.upsert(null, request, null));
        verify(databaseProvider, never()).saveUser(any(), any());
    }

    @Test
    void updateEncodesPasswordWhenProvided() {
        UUID userId = UUID.randomUUID();
        UserRequest request = UserRequest.builder()
                .password("NovaSenha@1")
                .build();

        UserResponse existing = UserResponse.builder()
                .id(userId)
                .name("Nome")
                .email("user@test.com")
                .password("hash-antigo")
                .build();

        when(databaseProvider.findUserById(userId)).thenReturn(existing);
        when(passwordEncoder.encode("NovaSenha@1")).thenReturn("hash-novo");
        when(databaseProvider.saveUser(any(), eq(userId)))
                .thenAnswer(inv -> UserResponse.builder()
                        .id(userId)
                        .password(((UserRequest) inv.getArgument(0)).getPassword())
                        .build());

        UserResponse result = userService.upsert(null, request, userId);

        assertEquals("hash-novo", result.getPassword());
        verify(passwordEncoder).encode("NovaSenha@1");
    }
}
