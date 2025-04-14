package com.br.obitus_manager.infrastructure.persistence.state;

import com.br.obitus_manager.domain.state.StateResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "estados")
public class StateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_estado", updatable = false, unique = true, nullable = false)
    private UUID id;

    @Column(name = "nome", nullable = false)
    private String name;

    @Column(name = "sigla", nullable = false)
    private String acronym;

    @Column(name = "ativo", nullable = false)
    private Boolean active;

    public StateResponse toModel() {
        return StateResponse.builder()
                .id(this.id)
                .name(this.name)
                .acronym(this.acronym)
                .active(this.active)
                .build();
    }
}
