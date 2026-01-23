package br.com.ai;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LangChainController {

    @Autowired
    ChatLanguageModel chatLanguageModel;

    @Autowired
    Agente assistant;

    @PostMapping("/chat-bot")
    public String chatBot(@RequestBody String message) {
        return chatLanguageModel.generate(message);
    }

    @PostMapping("/assistant")
    public String assistant(@RequestBody String message) {
        return assistant.chat(message);
    }

}
