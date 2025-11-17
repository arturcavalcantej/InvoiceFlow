-- Ajuste de tipos de data/hora para TIMESTAMPTZ e criação de constraints/colunas
-- Execute como V2 (NÃO altere a V1 já aplicada)

-- SUPPLIER: TEXT -> TIMESTAMPTZ
ALTER TABLE supplier
ALTER COLUMN created_at TYPE TIMESTAMPTZ USING created_at::timestamptz,
  ALTER COLUMN updated_at TYPE TIMESTAMPTZ USING updated_at::timestamptz;

-- Opcional: defaults
ALTER TABLE supplier
    ALTER COLUMN created_at SET DEFAULT NOW(),
ALTER COLUMN updated_at SET DEFAULT NOW();

-- INVOICE: TEXT -> TIMESTAMPTZ
ALTER TABLE invoice
ALTER COLUMN created_at TYPE TIMESTAMPTZ USING created_at::timestamptz,
  ALTER COLUMN updated_at TYPE TIMESTAMPTZ USING updated_at::timestamptz;

ALTER TABLE invoice
    ALTER COLUMN created_at SET DEFAULT NOW(),
ALTER COLUMN updated_at SET DEFAULT NOW();

-- Unicidade por fornecedor + numero + serie
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_constraint
     WHERE conname = 'uk_invoice_supplier_number_series'
  ) THEN
ALTER TABLE invoice
    ADD CONSTRAINT uk_invoice_supplier_number_series
        UNIQUE (supplier_id, number, series);
END IF;
END $$;

-- Índices úteis
CREATE INDEX IF NOT EXISTS idx_invoice_issue_date ON invoice(issue_date);
-- status/index já existe na V1

-- INVOICE_ITEM: (sem alterações estruturais aqui)

-- DOCUMENT_FILE: novos campos + timestamps consistentes
ALTER TABLE document_file
    ADD COLUMN IF NOT EXISTS original_name VARCHAR(200),
    ADD COLUMN IF NOT EXISTS size_bytes BIGINT;

-- tornar NOT NULL depois de popular (se necessário)
ALTER TABLE document_file
ALTER COLUMN updated_at TYPE TIMESTAMPTZ USING updated_at::timestamptz;

-- created_at para upload time (opcional, recomendado)
ALTER TABLE document_file
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMPTZ;
UPDATE document_file SET created_at = COALESCE(created_at, updated_at);
ALTER TABLE document_file
    ALTER COLUMN created_at SET NOT NULL,
ALTER COLUMN created_at SET DEFAULT NOW();

-- Deduplicação (opcional): checksum + type únicos
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_indexes WHERE indexname = 'uk_document_checksum_type'
  ) THEN
CREATE UNIQUE INDEX uk_document_checksum_type
    ON document_file (checksum, type);
END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_document_file_checksum ON document_file(checksum);

-- VALIDATION_RESULT: TIMESTAMP -> TIMESTAMPTZ (se quiser fuso)
ALTER TABLE validation_result
ALTER COLUMN validated_at TYPE TIMESTAMPTZ USING validated_at::timestamptz;

-- FKs e ON DELETE estão ok conforme V1
