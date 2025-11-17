package com.artur.invoiceflow.controller;

import com.artur.invoiceflow.domain.enums.DocumentType;
import com.artur.invoiceflow.dto.document.DocumentFileRequest;
import com.artur.invoiceflow.dto.document.DocumentFileResponse;
import com.artur.invoiceflow.service.DocumentFileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class DocumentFileController {

    private final DocumentFileService service;

    @PostMapping
    public DocumentFileResponse register(@RequestBody @Valid DocumentFileRequest body) {
        return service.register(body);
    }

    @GetMapping("/{id}")
    public DocumentFileResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public Page<DocumentFileResponse> list(
            @RequestParam(required = false) DocumentType type,      // PDF | XML
            @RequestParam(required = false) String checksum,
            Pageable pageable
    ) {
        return service.list(type, checksum, pageable);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
