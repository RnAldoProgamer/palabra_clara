package com.devspark.palabra_clara.repository;

import com.devspark.palabra_clara.entity.PalabraEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PalabraRepository extends CrudRepository<PalabraEntity, Long> {
    Optional<PalabraEntity> findByPalabra(String palabra);

    @Query("SELECT DISTINCT p.palabra FROM PalabraEntity p " +
            "UNION " +
            "SELECT DISTINCT v.variante FROM PalabraEntity p " +
            "JOIN p.variantes v " +
            "ORDER BY palabra")
    List<String> findAllPalabrasYVariantes();
}
