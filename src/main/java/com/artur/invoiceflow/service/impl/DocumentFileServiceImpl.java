package com.artur.invoiceflow.service.impl;

import com.artur.invoiceflow.domain.entity.DocumentFile;
import com.artur.invoiceflow.domain.enums.DocumentType;
import com.artur.invoiceflow.domain.repository.DocumentFileRepository;
import com.artur.invoiceflow.domain.repository.InvoiceRepository;
import com.artur.invoiceflow.dto.document.DocumentFileRequest;
import com.artur.invoiceflow.dto.document.DocumentFileResponse;
import com.artur.invoiceflow.service.DocumentFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DocumentFileServiceImpl implements DocumentFileService {

    private final DocumentFileRepository doc;
    private final InvoiceRepository invoiceRepo;

    @Override
    @Transactional
    public DocumentFileResponse register(DocumentFileRequest body) {
        Objects.requireNonNull(body, "body");

        // 1) valida existência da invoice
        var invoice = invoiceRepo.findById(body.invoiceId())
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found: " + body.invoiceId()));

        // 2) deduplicação por (checksum, type)
        if (doc.existsByChecksumAndType(body.checksum(), body.type())) {
            throw new IllegalArgumentException("Document already registered for checksum/type");
        }

        // 3) monta entidade
        var entity = DocumentFile.builder()
                .invoice(invoice)
                .type(body.type())
                .originalName(body.originalName())
                .sizeBytes(body.sizeBytes())
                .checksum(body.checksum())
                .storageKey(body.storageKey())
                .build();

        // 4) persiste
        var saved = doc.save(entity);

        // 5) retorna DTO
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<DocumentFileResponse> list(DocumentType type, String checksum, Pageable pageable) {

        if (type != null) {
            var t = DocumentType.valueOf(type.toUpperCase());
            return doc.findByType(t, pageable).map(this::toResponse);
        }
        if (checksum != null) {
            return doc.findByChecksum(checksum, pageable).map(this::toResponse);
        }
        return doc.findAll(pageable).map(this::toResponse);
    }


    @Override
    @Transactional(readOnly = true)
    public DocumentFileResponse get(Long id) {
        return doc.findById(id).map(this::toResponse).orElseThrow();
    }

    @Override
    public void delete(Long id) {
        doc.deleteById(id);
    }

    private DocumentFileResponse toResponse(DocumentFile f) {
        return new DocumentFileResponse(
                f.getId(),
                f.getType(),
                f.getOriginalName(),
                f.getSizeBytes(),
                f.getChecksum(),
                f.getStorageKey(),
                f.getUpdatedAt()
        );
    }
}
