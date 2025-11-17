package com.artur.invoiceflow.domain.enums;

public enum DocumentType { PDF, XML;

    public String toUpperCase() {
        return this.name().toUpperCase();
    }
}