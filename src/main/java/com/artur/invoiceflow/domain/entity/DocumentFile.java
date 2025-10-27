package com.artur.invoiceflow.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "document_file")
public class DocumentFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_id", nullable = false)
    private Long invoiceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType type;

    @Column(name = "storage_key", nullable = false)
    private String storageKey;

    @Column(nullable = false)
    private String checksum;

    @UpdateTimestamp
    @Column(nullable = false)
    private String updatedAt;

    public enum DocumentType {
        PDF, XML
    }
}
