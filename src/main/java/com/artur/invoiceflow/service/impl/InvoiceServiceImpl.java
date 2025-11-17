package com.artur.invoiceflow.service.impl;

import com.artur.invoiceflow.domain.entity.DocumentFile;
import com.artur.invoiceflow.domain.entity.Invoice;
import com.artur.invoiceflow.domain.entity.InvoiceItem;
import com.artur.invoiceflow.domain.repository.DocumentFileRepository;
import com.artur.invoiceflow.domain.repository.InvoiceRepository;
import com.artur.invoiceflow.domain.repository.SupplierRepository;
import com.artur.invoiceflow.dto.invoice.InvoiceItemResponse;
import com.artur.invoiceflow.dto.invoice.InvoiceRequest;
import com.artur.invoiceflow.dto.invoice.InvoiceResponse;
import com.artur.invoiceflow.dto.document.DocumentFileResponse;
import com.artur.invoiceflow.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import static java.util.Objects.requireNonNull;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository repo;
    private final SupplierRepository supplierRepo;
    private final DocumentFileRepository docRepo;


    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceResponse> list(Long supplierId, String status,
                                      LocalDate from, LocalDate to, Pageable pageable) {

        Specification<Invoice> bySupplier =
                (supplierId == null) ? null :
                        (root, q, cb) -> cb.equal(root.get("supplier").get("id"), supplierId);

        Specification<Invoice> byStatus = null;
        if (status != null && !status.isBlank()) {
            try {
                var st = Invoice.InvoiceStatus.valueOf(status.toUpperCase());
                byStatus = (root, q, cb) -> cb.equal(root.get("status"), st);
            } catch (IllegalArgumentException ignore) {
                // se quiser 400 em status inválido, lance IllegalArgumentException aqui
            }
        }

        Specification<Invoice> fromSpec =
                (from == null) ? null :
                        (root, q, cb) -> cb.greaterThanOrEqualTo(root.get("issueDate"), from);

        Specification<Invoice> toSpec =
                (to == null) ? null :
                        (root, q, cb) -> cb.lessThanOrEqualTo(root.get("issueDate"), to);

        Specification<Invoice> spec = Specification.allOf(bySupplier, byStatus, fromSpec, toSpec);

        return repo.findAll(spec, pageable).map(this::toResponse);
    }

    @Override
    @Transactional
    public InvoiceResponse create(InvoiceRequest body) {
        requireNonNull(body);

        var supplier = supplierRepo.findById(body.supplierId())
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));


        BigDecimal computedTotal = body.items().stream()
                .map(i -> i.unitPrice().multiply(BigDecimal.valueOf(i.qty())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (body.total() != null && body.total().compareTo(computedTotal) != 0) {
            throw new IllegalArgumentException("Total does not match items sum");
        }

        var inv = Invoice.builder()
                .supplier(supplier)
                .number(body.number())
                .series(body.series())
                .issueDate(body.issueDate() != null ? body.issueDate() : LocalDate.now())
                .total(computedTotal)
                .status(Invoice.InvoiceStatus.RECEIVED) // ajuste para seu enum
                .build();

        var items = body.items().stream().map(req ->
                InvoiceItem.builder()
                        .invoice(inv)
                        .sku(req.sku())
                        .description(req.description())
                        .qty(req.qty())
                        .unitPrice(req.unitPrice())
                        .subtotal(req.unitPrice().multiply(BigDecimal.valueOf(req.qty())))
                        .taxCode(req.taxCode())
                        .build()
        ).toList();
        inv.setItems(items);

        var saved = repo.save(inv);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse get(Long id) {
        return repo.findById(id).map(this::toResponse).orElseThrow();
     }

    @Transactional
    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    @Transactional
    public InvoiceResponse attachFiles(Long invoiceId, List<Long> fileIds) {
        requireNonNull(invoiceId, "invoiceId");
        requireNonNull(fileIds, "fileIds");

        // evita lista vazia/ids repetidos
        var ids = fileIds.stream().filter(Objects::nonNull).distinct().toList();
        if (ids.isEmpty()) {
            throw new IllegalArgumentException("fileIds must not be empty");
        }

        var invoice = repo.findById(invoiceId)
                .orElseThrow(() -> new NoSuchElementException("Invoice not found: " + invoiceId));

        // busca todos os arquivos
        var files = docRepo.findAllById(ids);

        // valida inexistentes
        if (files.size() != ids.size()) {
            var foundIds = files.stream().map(DocumentFile::getId).collect(Collectors.toSet());
            var missing = ids.stream().filter(id -> !foundIds.contains(id)).toList();
            throw new NoSuchElementException("Document(s) not found: " + missing);
        }

        // valida conflito: arquivo já anexado a outra invoice
        var conflicts = files.stream()
                .filter(f -> f.getInvoice() != null && !f.getInvoice().getId().equals(invoiceId))
                .map(DocumentFile::getId)
                .toList();
        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("File(s) already attached to another invoice: " + conflicts);
        }

        // associa (idempotente: se já estiver anexado à mesma, segue)
        files.forEach(f -> {
            if (f.getInvoice() == null || !f.getInvoice().getId().equals(invoiceId)) {
                f.setInvoice(invoice);
            }
        });
        docRepo.saveAll(files);

        // opcional: manter o lado inverso em memória
        if (invoice.getFiles() != null) {
            files.forEach(f -> {
                if (!invoice.getFiles().contains(f)) {
                    invoice.getFiles().add(f);
                }
            });
        }

        return toResponse(invoice);
    }



    private InvoiceResponse toResponse(Invoice inv) {
        var itemResponses = inv.getItems() == null ? List.<InvoiceItemResponse>of()
                : inv.getItems().stream().map(this::toInvoiceItemResponse).toList();

        var fileResponses = inv.getFiles() == null ? List.<DocumentFileResponse>of()
                : inv.getFiles().stream().map(this::toDocumentFileResponse).toList();

        return new InvoiceResponse(
                inv.getId(),
                inv.getSupplier().getId(),
                inv.getNumber(),
                inv.getSeries(),
                inv.getIssueDate(),
                inv.getTotal(),
                inv.getStatus().name(),
                itemResponses,
                fileResponses,
                inv.getCreatedAt(),
                inv.getUpdatedAt()
        );
    }

    private InvoiceItemResponse toInvoiceItemResponse(InvoiceItem it) {
        return new InvoiceItemResponse(
                it.getId(),
                it.getInvoice().getId(),
                it.getSku(),
                it.getDescription(),
                it.getQty(),
                it.getUnitPrice(),
                it.getSubtotal(),
                it.getTaxCode()
        );
    }
    private DocumentFileResponse toDocumentFileResponse(DocumentFile f) {
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
