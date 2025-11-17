package com.artur.invoiceflow.dto.document;

import com.artur.invoiceflow.domain.enums.DocumentType;
import java.time.OffsetDateTime;

public record DocumentFileResponse(
        Long id, DocumentType type, String originalName, Long sizeBytes,
        String checksum, String storageKey, OffsetDateTime uploadedAt
) {}
