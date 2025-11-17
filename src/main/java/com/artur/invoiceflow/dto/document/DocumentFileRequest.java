package com.artur.invoiceflow.dto.document;

import com.artur.invoiceflow.domain.enums.DocumentType;
import jakarta.validation.constraints.*;

public record DocumentFileRequest(
        @NotNull Long invoiceId,
        @NotNull DocumentType type,
        @NotBlank @Size(max = 200) String originalName,
        @NotNull @Positive Long sizeBytes,
        @NotBlank @Size(max = 255) String checksum,
        @NotBlank @Size(max = 255) String storageKey
) {}
