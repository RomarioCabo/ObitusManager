package com.br.obitus_manager.infrastructure.persistence.user;

import com.br.obitus_manager.domain.role.Role;
import com.br.obitus_manager.domain.user.UserRequest;
import com.br.obitus_manager.domain.user.UserResponse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuarios")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_usuario", updatable = false, unique = true, nullable = false)
    private UUID id;

    @Column(name = "nome")
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role", nullable = false)
    private String role = Role.ROLE_ADMIN.name();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UserEntity(final UUID id, final UserRequest request) {
        this.id = id;
        this.name = request.getName();
        this.email = request.getEmail();
        this.password = request.getPassword();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = id == null ? null : LocalDateTime.now();
    }

    public UserResponse toModel() {
        return UserResponse.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .role(this.role)
                .build();
    }
}
