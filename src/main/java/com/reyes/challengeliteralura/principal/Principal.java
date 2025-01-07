package com.reyes.challengeliteralura.principal;
import com.reyes.challengeliteralura.dto.AutorDTO;
import com.reyes.challengeliteralura.dto.LibroDTO;
import com.reyes.challengeliteralura.dto.LibrosListaDTO;
import com.reyes.challengeliteralura.model.Autor;
import com.reyes.challengeliteralura.model.Libro;
import com.reyes.challengeliteralura.service.AutorService;
import com.reyes.challengeliteralura.service.ConsumoAPI;
import com.reyes.challengeliteralura.service.ConvierteDatos;
import com.reyes.challengeliteralura.service.LibroService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal {
    private ConsumoAPI consumoAPI;
    private ConvierteDatos conversor;
    private LibroService libroService;
    private AutorService autorService;
    private Scanner teclado = new Scanner(System.in);

    public Principal (ConsumoAPI consumoAPI, ConvierteDatos conversor, LibroService libroService, AutorService autorService){
        this.consumoAPI = consumoAPI;
        this.conversor = conversor;
        this.libroService = libroService;
        this.autorService = autorService;
    }


    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por titulo
                    2 - Listar libro registrados
                    3 - Listar Autores registrados
                    4 - Listar Autores vidos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarlibro();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private void buscarlibro() {
        System.out.print("Ingrese el titulo (o el nombre del autor) del libro que desea buscar: ");
        String tituloLibro = teclado.nextLine();

        String url = "https://gutendex.com/books/?search=" + tituloLibro.replace(" ", "%20");
        String json = consumoAPI.obtenerDatos(url);
        LibrosListaDTO resultados = conversor.convertirDatos(json, LibrosListaDTO.class);
        List<LibroDTO> librosDTO = resultados.resultados();

        System.out.println(); // Salto de línea

        if(librosDTO.isEmpty()){
            System.out.println("• Libro no encontrado en la API.\n");
            return;
        }

        Optional<Libro> optionalLibro = libroService.obtenerLibroTitulo(resultados.resultados().stream().findFirst().get().titulo());
        if(optionalLibro.isPresent()){
            System.out.println("• El libro \'" + optionalLibro.get().getTitulo() + "\' ya se encuentra en la base de datos.");
        }else{
            // Crear un nuevo libro
            Libro libroNuevo = new Libro(resultados);

            // Buscar o crear autor para el nuevo libro
            AutorDTO primerAutorDTO = librosDTO.stream().findFirst().get().autores().get(0);
            Optional<Autor> autor = autorService.obtenerAutorPorNombre(primerAutorDTO.nombre());
            if(!autor.isPresent()){
                Autor autorNuevo = new Autor(resultados);
                autorService.crearAutor(autorNuevo);

                // Asignar autor al libro nuevo
                libroNuevo.setAutor(autorNuevo);

                // Guardar libro en la base de datos
                libroService.guardarLibro(libroNuevo);
                System.out.println(libroNuevo);
                System.out.println("• Libro guardado correctamente.");
            }
        }

        System.out.println(); // Salto de línea
    }
}
