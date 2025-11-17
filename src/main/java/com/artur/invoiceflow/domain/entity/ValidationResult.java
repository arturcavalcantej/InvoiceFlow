package com.artur.invoiceflow.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "validation_result")
public class ValidationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // relação JPA (recomendado)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "rules_passed", columnDefinition = "jsonb")
    private Map<String, Object> rulesPassed;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "rules_failed", columnDefinition = "jsonb")
    private Map<String, Object> rulesFailed;

    @Column(nullable = false)
    private Integer score;

    @Column(name = "validated_at", nullable = false)
    private OffsetDateTime validatedAt;
}
