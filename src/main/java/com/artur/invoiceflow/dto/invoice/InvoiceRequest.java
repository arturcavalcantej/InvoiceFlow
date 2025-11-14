package com.artur.invoiceflow.dto.invoice;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;


public record InvoiceRequest(
        @NotNull
        Long supplierId,
        @NotBlank @Size(max = 40)
        String number,
        @Size(max = 20)
        String series,
        @NotNull
        LocalDate issueDate,
        // opcional: o serviço pode recalcular a partir dos itens e validar
        @DecimalMin("0.00")
        BigDecimal total,
        @NotNull @Size(min = 1)
        List<InvoiceItemRequest> items,
        // IDs de arquivos já “registrados” (PDF/XML) vinculados a esta invoice
        List<Long> fileIds
) {}