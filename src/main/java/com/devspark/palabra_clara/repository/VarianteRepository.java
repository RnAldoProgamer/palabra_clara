package com.devspark.palabra_clara.repository;

import com.devspark.palabra_clara.entity.VarianteEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VarianteRepository extends CrudRepository<VarianteEntity, Long> {

    Optional<VarianteEntity> findByVariante(String variante);
}
