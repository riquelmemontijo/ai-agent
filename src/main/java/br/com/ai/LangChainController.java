package br.com.ai;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class LangChainController {

    @Autowired
    ChatLanguageModel chatLanguageModel;

    @Autowired
    Agente assistant;

    @Autowired
    DocumentIngestionService documentIngestionService;

    @PostMapping("/chat-bot")
    public String chatBot(@RequestBody String message) {
        return chatLanguageModel.generate(message);
    }

    @PostMapping("/assistant")
    public String assistant(@RequestBody String message) {
        return assistant.chat(message);
    }

    @PostMapping("/documents/upload")
    public ResponseEntity<String> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            documentIngestionService.ingestDocument(file.getInputStream(), file.getOriginalFilename());
            return ResponseEntity.ok("Documento ingerido com sucesso: " + file.getOriginalFilename());
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao processar documento: " + e.getMessage());
        }
    }
}
