package com.artur.invoiceflow.dto.supplier;

import java.time.OffsetDateTime;

public record SupplierResponse(
        Long id,
        String cnpj,
        String name,
        String email,
        String phone,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}
