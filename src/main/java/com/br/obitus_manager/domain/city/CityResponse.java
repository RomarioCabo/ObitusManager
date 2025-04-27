package com.br.obitus_manager.domain.city;

import com.br.obitus_manager.domain.state.StateResponse;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CityResponse {
    private UUID id;
    private String name;
    private StateResponse state;
}
