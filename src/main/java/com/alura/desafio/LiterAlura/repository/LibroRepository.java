package com.alura.desafio.LiterAlura.repository;

import com.alura.desafio.LiterAlura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByIdLibro(Long idLibro);
    List<Libro> findByIdioma(String idioma);
    List<Libro> findTop10ByOrderByNumeroDeDescargasDesc();

    @Query("SELECT DISTINCT l.idioma FROM Libro l ORDER BY l.idioma")
    List<String> obtenerListaDeIdioma();

    @Query("SELECT l FROM Autor a JOIN a.libros l WHERE a.idAutor = :id")
    List<Libro> obtenerLibrosPorAutor(Long id);
}
