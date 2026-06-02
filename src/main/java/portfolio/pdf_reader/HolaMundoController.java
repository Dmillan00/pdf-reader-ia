package portfolio.pdf_reader;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ai.ollama.OllamaChatModel;


@RestController
public class HolaMundoController {

    //Declaramos el chat ia

    private final OllamaChatModel chatModel;

    // lo inyectamos como dependenica en la clase

    public HolaMundoController(OllamaChatModel chatModel){
        this.chatModel = chatModel;
    }


    @GetMapping("/ia")
    public String preguntarIA(@RequestParam(value = "mensaje", defaultValue = "Presentate en uan frase") String mensaje) {
        //mandar el mensaje a la IA i devolvemos lo que mande
        return chatModel.call(mensaje);
    }

}
