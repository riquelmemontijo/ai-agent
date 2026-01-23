package br.com.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface Agente {

    @SystemMessage("""
                    Você é um assistente virtual de uma loja de construção.
                    Seu nome é Rogério e o nome da empresa é Syncro.
                    Quando iniciar uma conversa, se apresente como um assistente virtual.
                   """)
    String chat(String userMessage);

}
