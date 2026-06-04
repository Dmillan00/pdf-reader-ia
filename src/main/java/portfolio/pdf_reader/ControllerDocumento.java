package portfolio.pdf_reader;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ai.document.Document;

import java.util.List;

import org.springframework.ai.ollama.OllamaChatModel;

@RestController
public class ControllerDocumento {

    // Declaramos el chat ia

    private final ServicioDocumento servicioDocumento;
    private final ServicioIa servicioIa;
    private final OllamaChatModel chatModel;

    // lo inyectamos como dependenica en la clase

    public ControllerDocumento(OllamaChatModel chatModel, ServicioDocumento servicioDocumento, ServicioIa servicioIa) {
        this.chatModel = chatModel;
        this.servicioDocumento = servicioDocumento;
        this.servicioIa = servicioIa;
    }

    @GetMapping("/ia")
    public String preguntarIA(
            @RequestParam(value = "mensaje", defaultValue = "Presentate en uan frase") String mensaje) {
        // mandar el mensaje a la IA y devolvemos lo que mande
        return chatModel.call(mensaje);
    }

    // guardar documento
    @GetMapping("/guardarDocumento")
    public String guardarDocumento(@RequestParam(value = "nombre") String nombre,
            @RequestParam(value = "contenido") String contenido) {
        servicioDocumento.guardarDocumento(nombre, contenido);
        return "!Documento guardado";
    }

    // mostrat documentos guardados

    @GetMapping("/mostrarDocumento")
    public List<Documento> mostrarDocumentos() {
        return servicioDocumento.listarDocumentos();
    }

    // guardar vector

    @GetMapping("/guardar-vector")
    public String guardarVector(@RequestParam String texto) {
        servicioIa.guardarTextoEnVectores(texto);
        return "!Vector guardado";
    }

    // buscar en vectores

    @GetMapping("/buscar")
    public List<String> buscar(@RequestParam String query) {
        return servicioIa.buscarEnVectores(query).stream()
                .map(Document::getText) // <--- Cambia getContent() por getText()
                .toList();
    }

}
