package portfolio.pdf_reader;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ServicioDocumento {

    // Añadimos el repositorio

    private final RepositorioDocumento repositorioDocumento;

    public ServicioDocumento (RepositorioDocumento repositorioDocumento){
        this.repositorioDocumento = repositorioDocumento;

    }

    // Guardar documento nuevo
    public Documento guardarDocumento(String nombre, String contenido){
        Documento nuevoDocumento = new Documento(nombre, contenido);
        return repositorioDocumento.save(nuevoDocumento);
    }

    //Mostrar todos los documentos
    public List<Documento> listarDocumentos(){
        return repositorioDocumento.findAll();
    }

}
