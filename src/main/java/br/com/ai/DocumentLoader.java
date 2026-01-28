package br.com.ai;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
public class DocumentLoader {

    private static final Logger log = LoggerFactory.getLogger(DocumentLoader.class);

    private final DocumentIngestionService documentIngestionService;

    @Value("${documents.directory:src/main/resources/documents}")
    private String documentsDirectory;

    public DocumentLoader(DocumentIngestionService documentIngestionService) {
        this.documentIngestionService = documentIngestionService;
    }

    @PostConstruct
    public void loadDocumentsOnStartup() {
        Path documentsPath = Paths.get(documentsDirectory);

        if (!Files.exists(documentsPath)) {
            log.info("Diretório de documentos não existe. Criando: {}", documentsPath);
            try {
                Files.createDirectories(documentsPath);
                log.info("Diretório criado. Adicione documentos em: {}", documentsPath.toAbsolutePath());
            } catch (IOException e) {
                log.error("Erro ao criar diretório de documentos", e);
            }
            return;
        }

        log.info("Carregando documentos do diretório: {}", documentsPath.toAbsolutePath());

        try (Stream<Path> paths = Files.walk(documentsPath)) {
            paths.filter(Files::isRegularFile)
                 .filter(path -> {
                     String fileName = path.getFileName().toString().toLowerCase();
                     return fileName.endsWith(".txt") ||
                            fileName.endsWith(".pdf") ||
                            fileName.endsWith(".doc") ||
                            fileName.endsWith(".docx");
                 })
                 .forEach(path -> {
                     try {
                         log.info("Processando documento: {}", path.getFileName());
                         documentIngestionService.ingestDocument(path);
                         log.info("Documento processado com sucesso: {}", path.getFileName());
                     } catch (Exception e) {
                         log.error("Erro ao processar documento: {}", path.getFileName(), e);
                     }
                 });

            log.info("Carregamento de documentos concluído!");

        } catch (IOException e) {
            log.error("Erro ao ler diretório de documentos", e);
        }
    }
}
