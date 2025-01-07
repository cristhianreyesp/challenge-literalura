package com.reyes.challengeliteralura.model;

import com.reyes.challengeliteralura.dto.LibrosListaDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String lenguajes;
    private int cantDescargas;

    @ManyToOne
    @JoinColumn(name = "id_autor")
    private Autor autor;

    public Libro(){}

    public Libro(LibrosListaDTO libroLista) {
        this.titulo = libroLista.resultados().stream().findFirst().get().titulo();
        this.lenguajes = libroLista.resultados().stream().findFirst().get().lenguajes().stream().findFirst().get();
        this.cantDescargas = libroLista.resultados().stream().findFirst().get().cantDescargas();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLenguajes() {
        return lenguajes;
    }

    public void setLenguajes(String lenguajes) {
        this.lenguajes = lenguajes;
    }

    public int getCantDescargas() {
        return cantDescargas;
    }

    public void setCantDescargas(int cantDescargas) {
        this.cantDescargas = cantDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString(){
        return "Titulo: " + titulo +
                "\nAutor: " + (autor != null ? autor.getNombre() : "N/A") +
                "\nLenguaje: " + lenguajes +
                "\nNúmero de descargas: " + cantDescargas + "\n";
    }
}
