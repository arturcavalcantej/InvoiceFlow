package com.artur.invoiceflow.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "supplier")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,  length = 14)
    private String cnpj;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 13)
    private String phone;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private String createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private String updatedAt;
}
