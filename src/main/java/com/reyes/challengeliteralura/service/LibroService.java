package com.reyes.challengeliteralura.service;
import com.reyes.challengeliteralura.model.Libro;
import com.reyes.challengeliteralura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    public void guardarLibro(Libro libro){
        libroRepository.save(libro);
    }

//    public List<Libro> listarLibros(){
//        return libroRepository.findAll();
//    }

//    public List<Libro> libroPorLenguajes(String lenguajes){
//        return libroRepository.findByl(lenguajes);
//    }

    public Optional<Libro> obtenerLibroTitulo(String titulo){
        return libroRepository.findByTitulo(titulo);
    }
}
