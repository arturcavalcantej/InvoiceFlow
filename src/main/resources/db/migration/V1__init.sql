-- Flyway initial migration: create core tables for InvoiceFlow
-- PostgreSQL dialect

CREATE TABLE IF NOT EXISTS supplier (
    id BIGSERIAL PRIMARY KEY,
    cnpj VARCHAR(14) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(13) NOT NULL,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS invoice (
    id BIGSERIAL PRIMARY KEY,
    supplier_id BIGINT NOT NULL REFERENCES supplier(id) ON DELETE RESTRICT,
    number VARCHAR(255) NOT NULL,
    series VARCHAR(255) NOT NULL,
    issue_date DATE NOT NULL,
    total NUMERIC(10,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    raw_text TEXT,
    errors JSONB,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL,
    CONSTRAINT invoice_status_chk CHECK (status IN ('RECEIVED','EXTRACTED','VALIDATED','REJECTED'))
);

CREATE INDEX IF NOT EXISTS idx_invoice_supplier_id ON invoice(supplier_id);
CREATE INDEX IF NOT EXISTS idx_invoice_status ON invoice(status);

CREATE TABLE IF NOT EXISTS invoice_item (
    id BIGSERIAL PRIMARY KEY,
    invoice_id BIGINT NOT NULL REFERENCES invoice(id) ON DELETE CASCADE,
    sku VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    qty INT NOT NULL,
    unit_price NUMERIC(10,2) NOT NULL,
    tax_code VARCHAR(255) NOT NULL,
    subtotal NUMERIC(10,2) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_invoice_item_invoice_id ON invoice_item(invoice_id);

CREATE TABLE IF NOT EXISTS document_file (
    id BIGSERIAL PRIMARY KEY,
    invoice_id BIGINT NOT NULL REFERENCES invoice(id) ON DELETE CASCADE,
    type VARCHAR(20) NOT NULL,
    storage_key VARCHAR(255) NOT NULL,
    checksum VARCHAR(255) NOT NULL,
    updated_at TEXT NOT NULL,
    CONSTRAINT document_file_type_chk CHECK (type IN ('PDF','XML'))
);

CREATE INDEX IF NOT EXISTS idx_document_file_invoice_id ON document_file(invoice_id);

CREATE TABLE IF NOT EXISTS validation_result (
    id BIGSERIAL PRIMARY KEY,
    invoice_id BIGINT NOT NULL REFERENCES invoice(id) ON DELETE CASCADE,
    rules_passed JSONB,
    rules_failed JSONB,
    score INT NOT NULL,
    validated_at TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_validation_result_invoice_id ON validation_result(invoice_id);
