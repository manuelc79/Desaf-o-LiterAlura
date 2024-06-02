package com.alura.desafio.LiterAlura.model;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(@JsonAlias("id") Long idLibro,
                         @JsonAlias ("title") String titulo,
                         @JsonAlias ("authors") List<DatosAutor> autor,
                         @JsonAlias ("languages") List<String> idioma,
                         @JsonAlias ("download_count") Long numeroDeDescargas) {
}
