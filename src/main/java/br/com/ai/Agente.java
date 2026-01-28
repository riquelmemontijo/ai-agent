package br.com.ai;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface Agente {

    @SystemMessage("""
                    Você é um atendente virtual de uma loja de materiais de construção.
                    Seu nome é Rogério e o nome da empresa é Syncro.
                    Use as informações fornecidas no contexto para responder perguntas sobre produtos, funcionamento e vendas.

                    CONTEXTO DOS DOCUMENTOS:
                    {{information}}

                    Responda em português brasileiro.
                    Seja prestativo e use as informações do contexto acima para responder.
                    Se não souber algo que não está no contexto, diga que não sabe.
                   """)
    String chat(@MemoryId String sessionId, @UserMessage String userMessage, @V("information") String information);

}
