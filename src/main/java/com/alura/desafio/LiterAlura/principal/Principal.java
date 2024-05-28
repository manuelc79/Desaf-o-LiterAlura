package com.alura.desafio.LiterAlura.principal;

import com.alura.desafio.LiterAlura.model.Autor;
import com.alura.desafio.LiterAlura.model.DatosLibro;
import com.alura.desafio.LiterAlura.model.Libros;
import com.alura.desafio.LiterAlura.repository.LibroRepository;
import com.alura.desafio.LiterAlura.service.ConsumoAPI;
import com.alura.desafio.LiterAlura.service.ConvierteDatos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private String URL_BASE = "https://gutendex.com/book?";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosLibro> datosLibros = new ArrayList<>();
    private LibroRepository repositorio;
    private List<Libros> libros;
    private List<Autor> autores;
    
//    public Principal(LibroRepository repository){
//        this.repositorio = repository;
//    }
    
    public void mostrarElMenu() {
        var opcion = -1;
        while (opcion != 0){
            var menu = """
                    1 - Buscar Libro por Título
                    2 - Listar Libros Registrados
                    3 - Listar Autores Registrados
                    4 - Listar Autores Vivos en un determinado Año
                    5 - Listar Libros por Idioma
                    
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();
            
            switch (opcion){
                case 1: {
                    buscarLibroWeb();
                    break;
                }
                case 2: {
                    listarLibrosRegistrados();
                    break;
                }
                case 3: {
                    listarAutoresRegistrados();
                    break;
                }
                case 4: {
                    listarAutoresVivosPorAño();
                    break;
                }
                case 5: {
                    listarLibrosPorIdioma();
                    break;
                }
                case 0: {
                    System.out.println("Cerrando la aplicación...");
                    System.out.println(opcion);
                    break;
                }
                default:
                    System.out.println("opción inválida");
            }
        }
    }

    private void buscarLibroWeb() {

    }

    private void listarLibrosRegistrados() {
    }

    private void listarAutoresRegistrados() {
    }

    private void listarAutoresVivosPorAño() {
    }

    private void listarLibrosPorIdioma() {
    }


}
