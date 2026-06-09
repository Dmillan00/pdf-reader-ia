package portfolio.pdf_reader;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

//leer pdf

import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;

@Service
public class ServicioIa {

    // inyectar vector Store
    private final VectorStore vectorStore;

    public ServicioIa(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    // ------ METODO PARA PROCESAR EL PDF Y GUARDARLO EN LA BASE DE DATOS DE VECTORES ------

    public void procesarYGuardarPdf(MultipartFile archivo) {
    File archivoTemporal = null;
    try {
        // 1. Forzamos la creacion de un archivo fisico temporal para que el lector no falle
        archivoTemporal = File.createTempFile("subida_", ".pdf");
        Files.copy(archivo.getInputStream(), archivoTemporal.toPath(), StandardCopyOption.REPLACE_EXISTING);
        
        // 2. Leemos el PDF usando el lector de Spring AI
        Resource pdfRecurso = new FileSystemResource(archivoTemporal);
        PagePdfDocumentReader pdfLector = new PagePdfDocumentReader(pdfRecurso);

        // Leemos el PDF y obtenemos una lista de páginas como Documentos
        List<Document> paginas = pdfLector.get(); 

        // 3. Dividir el texto en fragmentos mas pequeños (chunks)
       TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> fragmentos = splitter.apply(paginas);

        //nombre del archivo
        for(Document doc : fragmentos) {
            doc.getMetadata().put("nombreArchivo", archivo.getOriginalFilename());
        }

        // 5. Guardar en la base de datos de vectores
        this.vectorStore.add(fragmentos);
        System.out.println("El pdf con nombre " + archivo.getOriginalFilename() + " se ha procesado y guardado correctamente con " + fragmentos.size() + " fragmentos.");
         
    } catch (Exception e) {
        throw new RuntimeException("Error al procesar el archivo PDF: " + e.getMessage(), e);
    } finally {
        // Limpieza del archivo temporal del disco
        if (archivoTemporal != null && archivoTemporal.exists()) {
            archivoTemporal.delete();
        }
    }
}

    //----- METODO PARA GUARDAR TEXTO EN LA BASE DE DATOS DE VECTORES -----
    public void guardarTextoEnVectores(String texto){
        Document documentoIa = new Document(texto);

        // convertir el texto a vector y guardarlo en el vector store
        vectorStore.add(List.of(documentoIa));
    }

    //----- METODO PARA BUSCAR EN LA BASE DE DATOS DE VECTORES -----
    public List<Document> buscarEnVectores(String query) {
    // Busca los documentos más parecidos (similarity search)
    return vectorStore.similaritySearch(query);
}

}
