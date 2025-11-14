package com.artur.invoiceflow.dto.invoice;
import com.artur.invoiceflow.dto.document.DocumentFileResponse;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.math.BigDecimal;
import java.util.List;

public record InvoiceResponse(
        Long id,
        Long supplierId,
        String number,
        String series,
        LocalDate issueDate,
        BigDecimal total,
        String status, // RECEIVED | EXTRACTED | VALIDATED | REJECTED
        List<InvoiceItemResponse> items,
        List<DocumentFileResponse> files,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}
