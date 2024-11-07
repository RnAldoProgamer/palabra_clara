package com.devspark.palabra_clara.entity;

import com.devspark.palabra_clara.util.StaticConstants;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = StaticConstants.TABLA_PALABRA)
public class PalabraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String palabra;

    private String path;

    @OneToMany(targetEntity = VarianteEntity.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = StaticConstants.TABLA_PALABRA, orphanRemoval = true)
    @JsonManagedReference
    private List<VarianteEntity> variantes;

    public void setVariantes(List<VarianteEntity> variantes) {
        this.variantes = variantes;
        if (variantes != null) {
            for (VarianteEntity variante : variantes) {
                variante.setPalabra(this); // Establecer la relación bidireccional
            }
        }
    }
}


