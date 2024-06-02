package com.alura.desafio.LiterAlura.principal;

import com.alura.desafio.LiterAlura.model.*;
import com.alura.desafio.LiterAlura.repository.AutorRepository;
import com.alura.desafio.LiterAlura.repository.LibroRepository;
import com.alura.desafio.LiterAlura.service.ConsumoAPI;
import com.alura.desafio.LiterAlura.service.ConvierteDatos;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private static final String URL_BASE = "http://gutendex.com/books/?search=";
    private static final String URL_LANGUAGE_CODE = "https://wiiiiams-c.github.io/language-iso-639-1-json-spanish/language-iso-639-1.json";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosLibro> datosLibros = new ArrayList<>();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private Libro libro;
    private List<Libro> libros;
    private List<Autor> autores;
    private Optional<Autor> autor;
    
    public Principal(LibroRepository libroRepository, AutorRepository autorRepository){

        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }
    
    public void mostrarElMenu() {
        var opcion = -1;
        while (opcion != 0){
            var menu = """
                    +===================================================+
                    |                 SISTEMA LITER-ALURA               |
                    +===================================================+
                    |                                                   |
                    |   1 - Buscar Libro por Título                     |
                    |   2 - Listar Libros Registrados                   |
                    |   3 - Listar Autores Registrados                  |
                    |   4 - Listar Autores Vivos en un determinado Año  |
                    |   5 - Listar Libros por Idioma                    |
                    |   6 - Los 5 libros más descargados                |
                    |                                                   |
                    |   0 - Salir                                       |
                    +---------------------------------------------------+
                    |   INGRESE UNA OPCION                              |
                    +===================================================|
                    """;
            try {
                System.out.println(menu);
                opcion = teclado.nextInt();
                teclado.nextLine();
            } catch (InputMismatchException e){
                opcion = -1;
                teclado.nextLine();
            }
            switch (opcion){
                case 0: {
                    System.out.println("Cerrando la aplicación...");
                    teclado.close();
                    break;
                }
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
                    listarAutoresVivosPorAnio();
                    break;
                }
                case 5: {
                    listarLibrosPorIdioma();
                    break;
                }
                case 6: {
                    top10Descargas();
                    break;
                }
                default:
                    System.out.println("\nOpción Inválida\n");
            }
        }
    }

    private void top10Descargas() {
        libros = libroRepository.findTop10ByOrderByNumeroDeDescargasDesc();

        if(libros.isEmpty()){
            System.out.println("No hay libros para mostrar");
        } else {
            var encabezadoTopTen = """
                    ==================================
                          Libros más descargados
                    ==================================
                    """;
            System.out.println(encabezadoTopTen);

            mostrarDatosLibros(libros);
        }
    }

    private Optional<DatosLibro> getDatosLibro() {
        System.out.println("Por favor ingrese el nombre del Libro que desea buscar");
        var nombreLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos( URL_BASE + nombreLibro.replace(" ", "%20"));
        var datos = conversor.obtieneDatos(json, ListaDeDatosLibros.class);

        Optional<DatosLibro> libroBuscado = datos.libros().stream()
                .filter(l -> l.titulo().toLowerCase().contains(nombreLibro.toLowerCase()))
                .findFirst();

        return libroBuscado;
    }

    private void buscarLibroWeb() {
        Optional<DatosLibro> datosLibro = getDatosLibro();
        if (datosLibro.isPresent()){
            DatosAutor datosAutor = datosLibro.get().autor().get(0);

            // validar existencia del libro en la base de datos
            if (libroRepository.findByIdLibro(datosLibro.get().idLibro()).isPresent()){
                System.out.println("El libro " +datosLibro.get().titulo() +", ya se encuentra almacenado en la base de datos...");
            } else {
                var encabezadoLibro = """
                        ===========================
                                LIBRO
                        ---------------------------
                        Titulo______: %s
                        Autor_______: %s
                        Idioma______: %s
                        N° Descargas: %s
                        ===========================
                        """;

                String nombreAutor = getNombre(datosLibro.get().autor().get(0).nombreAutor());
                System.out.println(encabezadoLibro.formatted(
                        datosLibro.get().titulo(),
                        nombreAutor,
                        datosLibro.get().idioma(),
                        datosLibro.get().numeroDeDescargas()
                ));

                //verificamos si el autor ya existe en la base de datos para no volver a guardarlo
                autor = autorRepository.findByNombreAutor(datosAutor.nombreAutor());
                if (autor.isPresent()){
                    libro = new Libro(datosLibro.get());
                    libro.setAutor(autor.get());
                    libroRepository.save(libro);
                } else {
                    libros = datosLibros.stream()
                            .map(l -> new Libro(l))
                            .collect(Collectors.toList());

                    Autor autorClass = new Autor(datosAutor);
                    autorClass.setLibros(libros);
                    autorRepository.save(autorClass);
                }

                System.out.println("El libro " + datosLibro.get().titulo() + ", se a guardado correctamente");
            }
        } else {
            System.out.println("Libro no encontrado, verifique el dato ingresado e intente nuevamente...");
        }
    }

    private String getNombre(String nombreAutor) {
        String[] partes = nombreAutor.split(", ");
        if(partes.length >=2) {
            String appellido = partes[0];
            String nombre = partes[1].trim();
            return nombre + " " + appellido;
        }
        return nombreAutor;
    }

    private void listarLibrosRegistrados() {
        libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados");
        } else {
            var encabezadoListaLibros = """
                =========================================
                |        LISTA DE LIBROS ALMACENADOS    |
                =========================================
                """;
            System.out.println(encabezadoListaLibros);
            var cantidadDeLibros = libros.size();
            mostrarDatosLibros(libros);

            System.out.println("Total de libros registrados: " + cantidadDeLibros);

        }
    }

    private void mostrarDatosLibros(List<Libro> libros) {
        var encabezadoLibro = """
                        ===========================
                                LIBRO
                        ---------------------------
                        Titulo______: %s
                        Autor_______: %s
                        Idioma______: %s
                        N° Descargas: %s
                        ===========================
                        """;

        libros.forEach(l -> System.out.println(
                encabezadoLibro.formatted(
                    l.getTitulo(),
                    getNombre(l.getAutor().getNombreAutor()),
                    l.getIdioma(),
                    l.getNumeroDeDescargas()
                )
        ));
    }

    private void listarAutoresRegistrados() {
        var encabezadoListaAutores = """
                ==================================
                |        AUTORES REGISTRADOS     |
                ==================================
                """;
        System.out.println(encabezadoListaAutores);

        autores = autorRepository.findAllByOrderByNombreAutorAsc();

        if (autores.isEmpty()){
            System.out.println("No hay autores registrados");
        } else {
            var cantidadAutores = autores.size();
            verAutores(autores);

            System.out.println("Total de autores listados: " + cantidadAutores);
        }
    }

    private void verAutores(List<Autor> listaAutores) {
        var muestraDatosAutor = """
                -------------------------------
                           DATOS DEL AUTOR
                -------------------------------
                NOMBRE:______________ %s
                AÑO DE NACIMIENTO:___ %s
                AÑO DE FALLECIMIENTO: %s
                LIBROS DE SU AUTORIA: %s
                """;
        listaAutores.forEach(a -> System.out.println(
                muestraDatosAutor.formatted(
                        a.getNombreAutor(),
                        (a.getAnioDeNacimiento()==null?"N/D" : a.getAnioDeNacimiento()),
                        (a.getAnioDeFallecimiento()==null?"N/D" : a.getAnioDeFallecimiento()),
                        libroRepository.obtenerLibrosPorAutor(a.getIdAutor()).stream()
                                .map(l -> l.getTitulo())
                                .collect(Collectors.joining(" | ", "[", "]"))
                )
        ));
    }

    private void listarAutoresVivosPorAnio() {
        try {
            System.out.println("Ingrese el año");
            int anio = teclado.nextInt();

            autores = autorRepository.obtenerAutorVivoSegunAnio(anio);

            var encabezadoAnioAutor = """
                    ==========================================
                            AUTORES VIVIOS EN EL AÑO %s
                    ==========================================
                    """;
            System.out.println(encabezadoAnioAutor.formatted(anio));

            if (autores.isEmpty()){
                System.out.println("No hay autores vivos para el año " + anio);
            } else {
                verAutores(autores);
                System.out.println("Total de autores" + autores.size());
            }
        } catch (InputMismatchException e) {
            System.out.println("El año ingresado no es valido");
            teclado.nextLine();
        }
    }

    private void listarLibrosPorIdioma() {
        var idiomaLibro = libroRepository.obtenerListaDeIdioma();
        var jsonIdioma = consumoAPI.obtenerDatos(URL_LANGUAGE_CODE);
        var datosIdioma = conversor.obtieneDatos(jsonIdioma, DatosIdioma.class);
        List<Idioma> idiomaObtenido = new ArrayList<>();

        if (idiomaLibro.isEmpty()) {
            System.out.println("No se encontraron idiomas de libros para listar");
        } else {
            for (String codigoIdioma : idiomaLibro) {
                var datoIdioma = datosIdioma.idiomas().stream()
                        .filter(i -> i.codigoIdioma().contains(codigoIdioma))
                        .collect(Collectors.toList());
                idiomaObtenido.add(datoIdioma.get(0));
            }

            var encabezadoListaDeIdiomas = """
                    ===================================
                        LISTA DE IDIOMAS DISPONIBLES
                    ===================================
                    LENGUAJE     ->          CODIGO
                    """;
            System.out.println(encabezadoListaDeIdiomas);
            idiomaObtenido.forEach(i -> System.out.println(i.idioma() + " -> " + i.codigoIdioma()));
            System.out.println("Ingrese el código del lenguaje que desea listar");

            String codigoIdioma = teclado.nextLine();

            if (verificarCodigo(codigoIdioma)){
                System.out.println("El código ingresado es inválido");
            } else {
                libros = libroRepository.findByIdioma(codigoIdioma);

                if (libros.isEmpty()){
                    System.out.println("No se encontró libros con el lenguaje seleccionado ");
                } else {
                    var cantidadLibros = libros.size();

                    mostrarDatosLibros(libros);

                    System.out.println("Total de libros " + cantidadLibros);
                }
            }
        }
    }

    private boolean verificarCodigo(String codigoIdioma) {
        Pattern expresionRegular = Pattern.compile("^[\\+-]?\\d+$");
        Matcher hacerMatch = expresionRegular.matcher(codigoIdioma);
        return hacerMatch.matches();
    }

}
