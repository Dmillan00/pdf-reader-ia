package portfolio.pdf_reader;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServicioIa {

    // inyectar vector Store
    private final VectorStore vectorStore;

    public ServicioIa(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

   

    //guardar fragmento de texto convertido a vector
    public void guardarTextoEnVectores(String texto){
        Document documentoIa = new Document(texto);

        // convertir el texto a vector y guardarlo en el vector store
        vectorStore.add(List.of(documentoIa));
    }

    public List<Document> buscarEnVectores(String query) {
    // Busca los documentos más parecidos (similarity search)
    // El '0.7' es el umbral de similitud, lo ajustamos para que no sea demasiado estricto
    return vectorStore.similaritySearch(query);
}

}
