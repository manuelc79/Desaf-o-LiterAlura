package com.alura.desafio.LiterAlura.repository;

import com.alura.desafio.LiterAlura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    List<Autor> findAllByOrderByNombreAutorAsc();
    Optional<Autor> findByNombreAutor(String nombreAutor);

    @Query("SELECT a FROM Autor a WHERE a.anioDeNacimiento < :fechaAnio AND a.anioDeFallecimiento > :fechaAnio ORDER BY a.nombreAutor ASC")
    List<Autor> obtenerAutorVivoSegunAnio(int fechaAnio);
}
