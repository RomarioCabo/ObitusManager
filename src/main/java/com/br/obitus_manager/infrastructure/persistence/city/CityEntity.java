package com.br.obitus_manager.infrastructure.persistence.city;

import com.br.obitus_manager.domain.city.CityRequest;
import com.br.obitus_manager.domain.city.CityResponse;
import com.br.obitus_manager.infrastructure.persistence.state.StateEntity;
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
@Table(name = "cidades")
public class CityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_cidade", updatable = false, unique = true, nullable = false)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_estado")
    private StateEntity stateEntity;

    @Column(name = "nome_cidade", nullable = false)
    private String name;

    public CityEntity(UUID idCity, StateEntity stateEntity, CityRequest request) {
        this.id = idCity;
        this.stateEntity = stateEntity;
        this.name = request.getName();
    }

    public CityResponse toModel() {
        return CityResponse.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }
}
