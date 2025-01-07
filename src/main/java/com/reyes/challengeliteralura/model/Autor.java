package com.reyes.challengeliteralura.model;

import java.util.List;
import java.util.stream.Collectors;

import com.reyes.challengeliteralura.dto.LibrosListaDTO;
import jakarta.persistence.*;

@Entity
@Table(name="autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_autor")
    private Long id_autor;

    @Column(unique = true)
    private String nombre;
    private int fechaNacimiento;
    private int fechaMuerte;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> librosAutor;

    public Autor(){}

    public Autor(LibrosListaDTO libroLista) {
        this.nombre = libroLista.resultados().stream().findFirst().get().autores().stream().findFirst().get().nombre();
        this.fechaNacimiento = libroLista.resultados().stream().findFirst().get().autores().stream().findFirst().get().fechaNacimiento();
        this.fechaMuerte = libroLista.resultados().stream().findFirst().get().autores().stream().findFirst().get().fechaMuerte();
    }

    public Long getId_autor() {
        return id_autor;
    }

    public void setId_autor(Long id_autor) {
        this.id_autor = id_autor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(int fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getFechaMuerte() {
        return fechaMuerte;
    }

    public void setFechaMuerte(int fechaMuerte) {
        this.fechaMuerte = fechaMuerte;
    }

    public List<Libro> getLibrosAutor() {
        return librosAutor;
    }

    public void setLibrosAutor(List<Libro> librosAutor) {
        this.librosAutor = librosAutor;
    }
    @Override
    public String toString(){
        return nombre + " (" + fechaNacimiento + "-" + fechaMuerte + ")\n" +
                "Libros: " + librosAutor.stream().map(l -> l.getTitulo()).collect(Collectors.toList()) + "\n";
    }
}
