package br.com.ai;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@Service
public class DocumentIngestionService {

    private final EmbeddingStore<TextSegment> embeddingStore;
    private final EmbeddingModel embeddingModel;
    private final DocumentSplitter documentSplitter;

    public DocumentIngestionService(EmbeddingStore<TextSegment> embeddingStore,
                                   EmbeddingModel embeddingModel) {
        this.embeddingStore = embeddingStore;
        this.embeddingModel = embeddingModel;
        this.documentSplitter = DocumentSplitters.recursive(300, 50);
    }

    public void ingestDocument(Path filePath) {
        Document document = loadDocument(filePath, new TextDocumentParser());
        ingestDocument(document);
    }

    public void ingestDocument(InputStream inputStream, String fileName) {
        TextDocumentParser parser = new TextDocumentParser();
        Document document = parser.parse(inputStream);
        ingestDocument(document);
    }

    private void ingestDocument(Document document) {
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(documentSplitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        ingestor.ingest(document);
    }

    public void ingestDocuments(List<Document> documents) {
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(documentSplitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        ingestor.ingest(documents);
    }
}
