-- Creanmos la extension para poder unsar el tipo de datos vector para guargar los embeddings de texto
CREATE EXTENSION IF NOT EXISTS vector;

-- Esquema de la tabla que se creara automaticamente al iniciar la aplicacion, esta tabla se usara para guardar los embeddings de texto y su metadata asociada
CREATE TABLE IF NOT EXISTS vector_store (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    content TEXT,
    metadata JSONB,
    embedding vector(768)
);