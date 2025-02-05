package com.devspark.palabra_clara.repository;

import com.devspark.palabra_clara.entity.VarianteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VarianteRepository extends CrudRepository<VarianteEntity, Long> {

    @Query("SELECT v FROM VarianteEntity v JOIN FETCH v.palabra WHERE LOWER(v.variante) = LOWER(:variante)")
    Optional<VarianteEntity> findByVarianteIgnoreCase(String variante);
}
