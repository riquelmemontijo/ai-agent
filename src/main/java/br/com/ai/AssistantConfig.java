package br.com.ai;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AssistantConfig {

    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return GoogleAiGeminiChatModel.builder()
                .apiKey("AIzaSyBbeXPmO-nff4EC1Qwr2sirioxRyjnNEXU")
                .modelName("gemini-3-flash-preview")
                .timeout(java.time.Duration.ofSeconds(60))
                .maxRetries(10)
                .build();
    }

    @Bean
    public ChatMemoryStore chatMemoryStore() {
        return new InMemoryChatMemoryStore();
    }

    @Bean
    public ChatMemory chatMemory(ChatMemoryStore chatMemoryStore) {
        return MessageWindowChatMemory.builder()
                .maxMessages(20) // Mantém até 20 mensagens (10 trocas de conversa)
                .chatMemoryStore(chatMemoryStore)
                .build();
    }

}
