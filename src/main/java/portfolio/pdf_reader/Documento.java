package portfolio.pdf_reader;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    private String nombreArchivo;

    @Column(columnDefinition = "TEXT")
    private String contenido;

    //Constructor

    public Documento(){

    }

    public Documento(String nombreArchivo, String contenido){
        this.nombreArchivo = nombreArchivo;
        this.contenido = contenido;
    }

    // GETTER Y SETTER

    public Long getId(){return Id;}
    public void setId(Long id){this.Id = id;}

    public String getNombreArchivo(){
        return nombreArchivo;
    }
    public void setNombreArchivo(String nombreArchivo){
        this.nombreArchivo = nombreArchivo;
    }

    public String getContenido(){
        return contenido;
    }
    public void setContenido(String contenido){
        this.contenido = contenido;
    }











}
