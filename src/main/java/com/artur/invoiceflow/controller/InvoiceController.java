package com.artur.invoiceflow.controller;

import com.artur.invoiceflow.dto.invoice.InvoiceRequest;
import com.artur.invoiceflow.dto.invoice.InvoiceResponse;
import com.artur.invoiceflow.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    private final InvoiceService service;

    @PostMapping
    public InvoiceResponse create(@RequestBody @Valid InvoiceRequest body) {
        return service.create(body);
    }

    @GetMapping("/{id}")
    public InvoiceResponse get(@PathVariable Long id) {
        // implemente o método get(id) no service quando quiser
        throw new UnsupportedOperationException("TODO: implementar get(id) no InvoiceService");
    }

    @GetMapping
    public Page<InvoiceResponse> list(
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            Pageable pageable
    ) {
        // se seu service hoje só tem list(Pageable), ajuste aqui também
        return service.list(supplierId, status, from, to, pageable);
        // quando evoluir:
        // return service.list(supplierId, status, from, to, pageable);
    }

    // opcional se fizer o “anexar arquivos depois”
    // @PostMapping("/{id}/files")
    // public InvoiceResponse attach(@PathVariable Long id, @RequestBody List<Long> fileIds) {
    //     return service.attachFiles(id, fileIds);
    // }
}
