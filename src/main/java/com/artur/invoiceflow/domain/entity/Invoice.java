package com.artur.invoiceflow.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
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
    @Column(nullable = false)
    private InvoiceStatus status;

    @Column(name = "raw_text", columnDefinition = "text")
    private String rawText;

    @Column(columnDefinition = "jsonb")
    private String errors;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private String createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private String updatedAt;

    public enum InvoiceStatus {
        RECEIVED, EXTRACTED, VALIDATED, REJECTED
    }

}
