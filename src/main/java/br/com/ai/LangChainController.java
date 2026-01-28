package br.com.ai;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LangChainController {

    @Autowired
    ChatLanguageModel chatLanguageModel;

    @Autowired
    Agente assistant;

    @Autowired
    DocumentIngestionService documentIngestionService;

    @Autowired
    ContentRetriever contentRetriever;

    @PostMapping("/chat-bot")
    public String chatBot(@RequestBody String message) {
        return chatLanguageModel.generate(message);
    }

    @PostMapping("/assistant")
    public String assistant(
            @RequestBody String message,
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId) {
        // Se o frontend não enviar sessionId, usa "default" (todos na mesma conversa)
        String memoryId = sessionId != null ? sessionId : "default";

        // Busca informações relevantes dos documentos usando RAG
        Query query = Query.from(message);
        List<Content> relevantContents = contentRetriever.retrieve(query);

        // Concatena os conteúdos relevantes
        String context = relevantContents.stream()
                .map(Content::textSegment)
                .map(segment -> segment.text())
                .collect(Collectors.joining("\n\n"));

        return assistant.chat(memoryId, message, context);
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
