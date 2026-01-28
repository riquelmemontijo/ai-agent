-- Habilitar extensão pgvector (necessário para armazenar embeddings)
CREATE EXTENSION IF NOT EXISTS vector;

-- Criar banco de dados (executar fora do banco se necessário)
-- CREATE DATABASE ai_agent_db;

-- O LangChain4j cria automaticamente a tabela 'embeddings' com createTable(true)
-- Mas caso queira criar manualmente:
/*
CREATE TABLE IF NOT EXISTS embeddings (
    id UUID PRIMARY KEY,
    embedding vector(384),  -- 384 dimensões para AllMiniLmL6V2EmbeddingModel
    text TEXT,
    metadata JSONB
);

-- Criar índice para busca vetorial eficiente
CREATE INDEX IF NOT EXISTS embeddings_embedding_idx
ON embeddings USING ivfflat (embedding vector_cosine_ops)
WITH (lists = 100);
*/
