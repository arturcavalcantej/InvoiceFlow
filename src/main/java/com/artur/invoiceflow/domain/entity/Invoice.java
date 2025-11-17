package com.artur.invoiceflow.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(
        name = "invoice",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_invoice_supplier_number_series",
                columnNames = {"supplier_id", "number", "series"}
        )
)
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private String series;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private InvoiceStatus status;

    @Column(name = "raw_text", columnDefinition = "text")
    private String rawText;

    // simples: jsonb como String (mantém sua V1/V2)
    @Column(columnDefinition = "jsonb")
    private String errors;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    // Navegação (opcional, mas recomendado)
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<InvoiceItem> items;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.PERSIST)
    private List<DocumentFile> files;

    public enum InvoiceStatus {
        RECEIVED, EXTRACTED, VALIDATED, REJECTED
    }
}
