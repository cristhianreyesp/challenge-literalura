package com.reyes.challengeliteralura.repository;


import com.reyes.challengeliteralura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("SELECT a FROM Autor a WHERE a.nombre = :nombre")
    Optional<Autor> findByNombre(@Param("nombre") String nombre);

    @Query("SELECT a FROM Autor a LEFT JOIN FETCH a.librosAutor  WHERE (a.fechaMuerte IS NULL OR a.fechaMuerte > :fecha) AND a.fechaNacimiento <= :fecha")
    List<Autor> findAutoresVivosConLibros(@Param("fecha") int fecha);

    @Query("SELECT a FROM Autor a LEFT JOIN FETCH a.librosAutor")
    List<Autor> findAllConLibros();
}
