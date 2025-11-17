package com.artur.invoiceflow.service;

import com.artur.invoiceflow.dto.invoice.InvoiceRequest;
import com.artur.invoiceflow.dto.invoice.InvoiceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceService {
    InvoiceResponse create(InvoiceRequest body);
    InvoiceResponse get(Long id);
    Page<InvoiceResponse> list(Long supplierId, String status,
                               LocalDate from, LocalDate to, Pageable pageable);
    InvoiceResponse attachFiles(Long invoiceId, List<Long> fileIds);
    void delete(Long id);
}
