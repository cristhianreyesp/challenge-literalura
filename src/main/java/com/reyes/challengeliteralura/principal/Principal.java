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
            var menu = "\n===========================\n"
                    + "📚  Biblioteca Virtual 📚\n"
                    + "===========================\n"
                    + "1️⃣  Buscar libro por título\n"
                    + "2️⃣  Listar libros registrados\n"
                    + "3️⃣  Listar autores registrados\n"
                    + "4️⃣  Listar autores vivos por año\n"
                    + "5️⃣  Listar libros por idioma\n"
                    + "0️⃣  Salir\n"
                    + "===========================\n"
                    + "🔎  Elige una opción: ";;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarlibro();
                    break;
                case 2:
                    listarLibrosregistrados();
                    break;
                case 3:
                    listarautoresregistrados();
                    break;
                case 4:
                    listarautoresvivos();
                    break;
                case 5:
                    listalibroidioma();
                    break;
                case 0:
                    System.out.println("\uD83D\uDED1 Cerrando...");
                    break;
                default:
                    System.out.println("\uD83D\uDED1 Opción inválida, por favor seleccione una opción válida.");
            }
        }
    }

    private void buscarlibro() {
        System.out.print("🔎 Ingrese el titulo del libro: ");
        String tituloLibro = teclado.nextLine();

        String url = "https://gutendex.com/books/?search=" + tituloLibro.replace(" ", "%20");
        String json = consumoAPI.obtenerDatos(url);
        LibrosListaDTO resultados = conversor.convertirDatos(json, LibrosListaDTO.class);
        List<LibroDTO> librosDTO = resultados.resultados();

        System.out.printf("\n");

        if(librosDTO.isEmpty()){
            System.out.println("❌ Libro no encontrado\n");
            return;
        }

        Optional<Libro> optionalLibro = libroService.obtenerLibroTitulo(resultados.resultados().stream().findFirst().get().titulo());
        if(optionalLibro.isPresent()){
            System.out.println("⚠\uFE0F El libro \'" + optionalLibro.get().getTitulo() + "\' ya se encuentra en la base de datos.");
        }else{

            Libro libroNuevo = new Libro(resultados);

            AutorDTO primerAutorDTO = librosDTO.stream().findFirst().get().autores().get(0);
            Optional<Autor> autor = autorService.obtenerAutorPorNombre(primerAutorDTO.nombre());
            if(!autor.isPresent()){
                Autor autorNuevo = new Autor(resultados);
                autorService.crearAutor(autorNuevo);

                libroNuevo.setAutor(autorNuevo);

                libroService.guardarLibro(libroNuevo);
                System.out.println(libroNuevo);
                System.out.println("✅ Libro guardado correctamente.");
            }
        }

        System.out.printf("\n");
    }


    private void listarLibrosregistrados() {
        List<Libro> libros = libroService.listarLibrosregistrados();

        if(libros.isEmpty()){
            System.out.println("⚠\uFE0F La lista de libros esta vacia.");
            return;
        }
        libros.forEach(l -> System.out.println(l));
    }

    private void listarautoresregistrados() {
        List<Autor> autores = autorService.listarAutores();

        if(autores.isEmpty()){
            System.out.println("⚠\uFE0F No hay datos registrados.");
            return;
        }
        autores.forEach(l -> System.out.println(l));
    }

    private void listarautoresvivos(){
        System.out.print("🔎 Escriba el año que desea buscar: ");
        int fecha = teclado.nextInt();

        List<Autor> autoresVivos = autorService.listarAutoresVivos(fecha);

        System.out.printf("\n");

        if(autoresVivos.isEmpty()){
            System.out.println("⚠\uFE0F No hay autores vivos en la fecha: " + fecha + ".\n");
            return;
        }

        autoresVivos.forEach(a -> {
            System.out.println("====================================");
            System.out.println("\uD83E\uDDD1\u200D\uD83C\uDFEB  Autor: " + a.getNombre());
            System.out.println("====================================");
            System.out.println("📅  Fecha de Nacimiento: " + a.getFechaNacimiento());
            System.out.println("✝️  Fecha de Muerte: " + a.getFechaMuerte());
            System.out.println("====================================");
            System.out.println("📚  Libros publicados: ");
            a.getLibrosAutor().forEach(libro ->
                    System.out.println("  - " + libro.getTitulo())
            );

            System.out.println("====================================\n");
        });

        System.out.printf("\n");
    }

    private void listalibroidioma() {
        System.out.println("====================================");
        System.out.println("  🌍  LENGUAJES DISPONIBLES ");
        System.out.println("====================================\n");
        System.out.println("es - Español");
        System.out.println("en - Inglés");
        System.out.println("fr - Francés");
        System.out.println("pt - Portugués");
        System.out.println("====================================\n");

        System.out.print("🔎 Selecciona un idioma: ");
        String lenguaje = teclado.nextLine().toLowerCase();
        System.out.printf("\n");

        switch (lenguaje) {
            case "es":
                System.out.println("📚 Seleccionaste Español.");
                break;
            case "en":
                System.out.println("📚 Seleccionaste Inglés.");
                break;
            case "fr":
                System.out.println("📚 Seleccionaste Francés.");
                break;
            case "pt":
                System.out.println("📚 Seleccionaste Portugués.");
                break;
            default:
                System.out.println("❌ La opción no es válida. Por favor, elige un idioma válido.\n");
                return;
        }

        List<Libro> libros = libroService.libroPorLenguajes(lenguaje);

        if (libros.isEmpty()) {
            System.out.println("🚫 No hay libros en ese idioma.\n");
            return;
        }

        System.out.println("====================================");
        System.out.println("  📖 LIBROS DISPONIBLES EN " + lenguaje.toUpperCase());
        System.out.println("====================================\n");

        libros.forEach(libro -> System.out.println("  - " + libro.getTitulo()));

        System.out.println("====================================\n");
    }

}
