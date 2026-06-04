-- src/main/resources/schema.sql
-- Esto crea la extensión de vectores en la base de datos si no existe
CREATE EXTENSION IF NOT EXISTS vector;

-- Esto crea tu tabla de vectores automáticamente
CREATE TABLE IF NOT EXISTS vector_store (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    content TEXT,
    metadata JSONB,
    embedding vector(768)
);