package com.artur.invoiceflow.dto.invoice;

import java.math.BigDecimal;


public record InvoiceItemResponse(
        Long id,
        Long invoiceId,
        String sku,
        String description,
        Integer qty,
        BigDecimal unitPrice,
        BigDecimal subtotal,
        String taxCode
) {}