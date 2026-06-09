package portfolio.pdf_reader;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ai.document.Document;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.ollama.OllamaChatModel;

@Controller
public class ControllerDocumento {

    // Declaramos el chat ia

    private final ServicioIa servicioIa;
    private final OllamaChatModel chatModel;
    List<String> historicoConversacion;

    // lo inyectamos como dependenica en la clase

    public ControllerDocumento(OllamaChatModel chatModel, ServicioIa servicioIa) {
        this.chatModel = chatModel;
        this.servicioIa = servicioIa;
        this.historicoConversacion = new ArrayList<>();
    }

    @GetMapping("/")
    public String index(Model modelo){
        modelo.addAttribute("historicoConversacion", historicoConversacion);
        return "index";
    }
    

    // ENDPOINT DE PRUEBA CON TEXTOS SIMPLES
    @ResponseBody
    @GetMapping("/ia-test")
    public String preguntarIA(
            @RequestParam(value = "mensaje", defaultValue = "Presentate en uan frase") String mensaje) {
        // mandar el mensaje a la IA y devolvemos lo que mande
        return chatModel.call(mensaje);
    }

    // ENDPOINT PARA GUARDAR TEXTO EN LA BASE DE DATOS DE VECTORES
    @ResponseBody
    @GetMapping("/guardarInfo")
    public String guardarVector(@RequestParam String txt) {
        servicioIa.guardarTextoEnVectores(txt);
        return "!Vector guardado";
    }

    // ENDPOINT PARA PROCESAR EL PDF Y GUARDARLO EN LA BASE DE DATOS DE VECTORES
    @PostMapping("/cargar-pdf")
    public String cargarPdf(@RequestParam("archivo") MultipartFile archivo,RedirectAttributes flash) {
        if (archivo.isEmpty()) {
            flash.addFlashAttribute("MensajeError", "El archivo seleccionado está vacío");
        }
        try {
            servicioIa.procesarYGuardarPdf(archivo);
            flash.addFlashAttribute("MensajeExito", "El archivo se ha procesado y guardado correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("MensajeError", "Error al procesar el archivo: " + e.getMessage());
        }
        return "redirect:/";
    }

    // ENDPOINT PARA RESPONDER A UNA PREGUNTA USANDO LA INFORMACION DE LOS PDF PROCESADOS
    @PostMapping("/ia")
    public String buscarYResponder(@RequestParam String mensaje) {

        // 1. Buscamos en la base de datos de vectores los fragmentos más relevantes para la pregunta del usuario
        List<String> fragmentos = servicioIa.buscarEnVectores(mensaje).stream()
                .map(Document::getText)
                .toList();

        // 2. Si no encontramos nada, devolvemos un mensaje
        if (fragmentos.isEmpty()) {
            return "No he encontrado información relevante sobre eso en tus documentos.";
        }

        // 3. Unimos los fragmentos para crear un contexto sólido
        String contexto = String.join("\n", fragmentos);
        historicoConversacion.add("Usuario: " + mensaje);



        // 4. Creamos el prompt para la IA
        String prompt = String.format(
                "Eres un asistente útil. Usa el siguiente contexto para responder a la pregunta del usuario. " +
                        "Contexto:\n%s\n\n" +
                        "Pregunta del usuario: %s",
                contexto, mensaje);

        // 5. Devolvemos la respuesta procesada por Llama3
        String respuestaIa =  chatModel.call(prompt);
        historicoConversacion.add("Ollama: " + respuestaIa);
        return "redirect:/";
    }

}
