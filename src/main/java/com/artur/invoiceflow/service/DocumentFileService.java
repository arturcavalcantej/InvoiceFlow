package com.artur.invoiceflow.service;

import com.artur.invoiceflow.domain.enums.DocumentType;
import com.artur.invoiceflow.dto.document.DocumentFileRequest;
import com.artur.invoiceflow.dto.document.DocumentFileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentFileService {
    DocumentFileResponse register(DocumentFileRequest body); // registra metadados (checksum, size, type, originalName)
    DocumentFileResponse get(Long id);
    Page<DocumentFileResponse> list(DocumentType type, String checksum, Pageable pageable);
    void delete(Long id);
}
