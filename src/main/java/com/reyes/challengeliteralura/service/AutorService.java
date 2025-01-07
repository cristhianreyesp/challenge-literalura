package com.reyes.challengeliteralura.service;

import com.reyes.challengeliteralura.model.Autor;
import com.reyes.challengeliteralura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    public void crearAutor(Autor autor){
        autorRepository.save(autor);
    }

    public List<Autor> listarAutores(){
        return autorRepository.findAllConLibros();
    }

//    public List<Autor> listarAutoresVivosEnAnio(int fecha){
//        return autorRepository.findAutoresVivosEnAnioConLibros(fecha);
//    }

    public Optional<Autor> obtenerAutorPorNombre(String nombre){
        return autorRepository.findByNombre(nombre);
    }
}
