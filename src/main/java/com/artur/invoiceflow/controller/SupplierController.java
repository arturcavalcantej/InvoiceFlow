package com.artur.invoiceflow.controller;

import com.artur.invoiceflow.dto.supplier.SupplierRequest;
import com.artur.invoiceflow.dto.supplier.SupplierResponse;
import com.artur.invoiceflow.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/suppliers")
public class SupplierController {

    private final SupplierService service;

    @PostMapping
    public SupplierResponse create(@RequestBody @Valid SupplierRequest body) {
        return service.create(body);
    }

    @PutMapping("/{id}")
    public SupplierResponse update(@PathVariable Long id, @RequestBody @Valid SupplierRequest body) {
        return service.update(id, body);
    }

    @GetMapping("/{id}")
    public SupplierResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public Page<SupplierResponse> list(
            @RequestParam(required = false) String q, // cnpj ou nome
            Pageable pageable
    ) {
        return service.list(q, pageable);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
