package com.br.obitus_manager.domain.state;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StateRequest {
    private UUID id;
    private Boolean active;
}
