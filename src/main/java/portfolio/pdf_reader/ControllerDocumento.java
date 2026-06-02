package portfolio.pdf_reader;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import org.springframework.ai.ollama.OllamaChatModel;


@RestController
public class ControllerDocumento {

    //Declaramos el chat ia

    private final ServicioDocumento servicioDocumento;
    private final OllamaChatModel chatModel;

    // lo inyectamos como dependenica en la clase

    public ControllerDocumento(OllamaChatModel chatModel, ServicioDocumento servicioDocumento){
        this.chatModel = chatModel;
        this.servicioDocumento = servicioDocumento;
    }


    @GetMapping("/ia")
    public String preguntarIA(@RequestParam(value = "mensaje", defaultValue = "Presentate en uan frase") String mensaje) {
        //mandar el mensaje a la IA y devolvemos lo que mande
        return chatModel.call(mensaje);
    }

    //guardar documento
    @GetMapping("/guardarDocumento")
    public String guardarDocumento() {
        servicioDocumento.guardarDocumento("Test de nombre ", "lorem ipsum sit amet");
        return "!Documento guardado";
    }

    // mostrat documetnos guardados

    @GetMapping("/mostrarDocumento")
    public List<Documento> mostrarDocumentos(){
        return servicioDocumento.listarDocumentos();
    }
}
