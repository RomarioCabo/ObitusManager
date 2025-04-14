package com.br.obitus_manager.domain.state;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StateResponse {
    private UUID id;
    private String name;
    private String acronym;
    private Boolean active;
}
