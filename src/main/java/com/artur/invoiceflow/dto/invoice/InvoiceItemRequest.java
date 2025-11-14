package com.artur.invoiceflow.dto.invoice;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;


public record InvoiceItemRequest(
        @NotBlank @Size(max = 60)
        String sku,
        @NotBlank @Size(max = 240)
        String description,
        @NotNull @Positive
        Integer qty,
        @NotNull @DecimalMin("0.00")
        BigDecimal unitPrice,
        @Size(max = 40)
        String taxCode
) {}