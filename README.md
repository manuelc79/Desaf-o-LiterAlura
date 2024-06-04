# Desafío LiterAlura
Aplcación para buscar libros unasndo la api de [Gutendex](https://gutendex.com/) en el cual se obtiene la información que estara almacenada en postgresql para su consulta

<br>

## Configuración

### Variables de Entorno

Debe configurar las siguientes variables de entorno para que la aplicación funcione:

<table border="1">
    <tr style="text-align: center;">
        <td>VARIABLE</td>
        <td>DESCRIPCIÓN</td>
    </tr>
    <tr>
        <td>DB_HOST</td>
        <td>ruta a la base de datos con su puerto</td>
    </tr>
    <tr>
        <td>DB_NAME</td>
        <td>Nombre de la base de datos: "liter_alura"</td>
    </tr>
    <tr>
        <td>DB_USR</td>
        <td>Usuario de la base de datos</td>
    </tr>
    <tr>
        <td>DB_PASSWORD</td>
        <td>Password de acceso a la base de datos</td>
    </tr>
</table>

<br>

## Aplicación

### *Menu*
Literalura consta de 5 opciones con las que podras interactuar, abajo imagen con las opciones:

1 - Buscar Libro por Título <br>
2 - Listar Libros Registrados <br>
3 - Listar Autores Registrados <br>
4 - Listar Autores Vivos en un determinado año <br> 
5 - Listar Libros por Idioma <br>
6 - Los 10 libros más descargados
             
0 - Salir

### *Buscar Libro por Título*
La aplicación solicita al usuario el nombre de un libro, lo busca usando la api antes nombrada,
si encuentra el libro verifica que no esté almacenada en la base de datos y lo guarda en la misma.


### *Listar Libros Registrados *
Esta opción lista todos los libros almacenados en la base de datos

### *Listar Autores Registrados *
Esta opción lista los autores almacenadoas en la base de datos


### *Listar Autores Vivos en un determinado año*
Solicita el ingreso de un año y el sistema busca los autores vivos en ese año


### *Listar Libros por Idioma*
Esta opción lista los idiomas disponibles, indicando cuál es el código que debe ingresar para que 
el sistema busque los libros del idioma ingresado.

### *Los 10 libros más descargados*
Esta opción muestra los 10 libros más descargados que estén almacenado en la base de datos 


## Alura Challenge Literalura Badge

<p align="center" width="100%">
    <img width="50%" src="https://storage.googleapis.com/media-github-readme/badge-literalura.png">
</p>