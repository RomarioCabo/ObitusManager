package com.br.obitus_manager.domain.user;

import lombok.*;

import static com.br.obitus_manager.domain.util.Util.maskEmail;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    private String name;
    private String email;
    private String password;

    @Override
    public String toString() {
        return "{\n" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"email\": \"" + maskEmail(email) + "\",\n" +
                "  \"password\": \"******\"\n" +
                "}";
    }
}
