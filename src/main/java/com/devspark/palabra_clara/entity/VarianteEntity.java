package com.devspark.palabra_clara.entity;

import com.devspark.palabra_clara.util.StaticConstants;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = StaticConstants.TABLA_VARIANTE)
public class VarianteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String variante;

    @ManyToOne(targetEntity = PalabraEntity.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = StaticConstants.COLUMNA_UNION_PALABRA_VARIANTE, referencedColumnName = StaticConstants.COLUMNA_UNION_PALABRA_REFERENCIADA)
    @JsonBackReference
    private PalabraEntity palabra;
}

