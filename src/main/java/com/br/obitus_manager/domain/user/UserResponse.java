package com.br.obitus_manager.domain.user;

import lombok.*;

import java.util.UUID;

import static com.br.obitus_manager.domain.util.Util.maskEmail;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String role;

    @Override
    public String toString() {
        return "{\n" +
                "  \"id\": \"" + id + "\",\n" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"email\": \"" + maskEmail(email) + "\",\n" +
                "  \"password\": \"******\",\n" +
                "  \"role\": \"" + role + "\"\n" +
                "}";
    }
}
