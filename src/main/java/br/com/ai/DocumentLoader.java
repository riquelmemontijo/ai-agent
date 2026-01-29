package br.com.ai;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class DocumentLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DocumentLoader.class);

    @Autowired
    private DocumentIngestionService documentIngestionService;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Carregando documentos...");
        List<Document> documents = loadDocumentsFromResources();
        documentIngestionService.ingestDocuments(documents);
        logger.info("{} documentos carregados.", documents.size());
    }

    private List<Document> loadDocumentsFromResources() throws IOException {
        List<Document> documents = new ArrayList<>();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:documents/*.md");

        for (Resource resource : resources) {
            Path path = Path.of(resource.getURI());
            documents.add(FileSystemDocumentLoader.loadDocument(path));
        }

        return documents;
    }
}
