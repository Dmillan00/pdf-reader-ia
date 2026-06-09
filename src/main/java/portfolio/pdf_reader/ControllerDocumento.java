package portfolio.pdf_reader;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ai.document.Document;

import java.util.List;

import org.springframework.ai.ollama.OllamaChatModel;

@RestController
public class ControllerDocumento {

    // Declaramos el chat ia

    private final ServicioIa servicioIa;
    private final OllamaChatModel chatModel;

    // lo inyectamos como dependenica en la clase

    public ControllerDocumento(OllamaChatModel chatModel, ServicioIa servicioIa) {
        this.chatModel = chatModel;
        this.servicioIa = servicioIa;
    }

    // ENDPOINT DE PRUEBA CON TEXTOS SIMPLES
    @GetMapping("/ia-test")
    public String preguntarIA(
            @RequestParam(value = "mensaje", defaultValue = "Presentate en uan frase") String mensaje) {
        // mandar el mensaje a la IA y devolvemos lo que mande
        return chatModel.call(mensaje);
    }

    // ENDPOINT PARA GUARDAR TEXTO EN LA BASE DE DATOS DE VECTORES

    @GetMapping("/guardarInfo")
    public String guardarVector(@RequestParam String txt) {
        servicioIa.guardarTextoEnVectores(txt);
        return "!Vector guardado";
    }

    // ENDPOINT PARA PROCESAR EL PDF Y GUARDARLO EN LA BASE DE DATOS DE VECTORES
    @PostMapping("/cargar-pdf")
    public String cargarPdf(@RequestParam("archivo") MultipartFile archivo) {
        if (archivo.isEmpty()) {
            return "Archivo vacío";
        }
        servicioIa.procesarYGuardarPdf(archivo);
        return "OK";
    }

    // ENDPOINT PARA RESPONDER A UNA PREGUNTA USANDO LA INFORMACION DE LOS PDF PROCESADOS
    @GetMapping("/ia")
    public String buscarYResponder(@RequestParam String txt) {
        // 1. Buscamos en la base de datos de vectores los fragmentos más relevantes para la pregunta del usuario
        List<String> fragmentos = servicioIa.buscarEnVectores(txt).stream()
                .map(Document::getText)
                .toList();

        // 2. Si no encontramos nada, devolvemos un mensaje
        if (fragmentos.isEmpty()) {
            return "No he encontrado información relevante sobre eso en tus documentos.";
        }

        // 3. Unimos los fragmentos para crear un contexto sólido
        String contexto = String.join("\n", fragmentos);

        // 4. Creamos el prompt para la IA
        String prompt = String.format(
                "Eres un asistente útil. Usa el siguiente contexto para responder a la pregunta del usuario. " +
                        "Contexto:\n%s\n\n" +
                        "Pregunta del usuario: %s",
                contexto, txt);

        // 5. Devolvemos la respuesta procesada por Llama3
        return chatModel.call(prompt);
    }

}
