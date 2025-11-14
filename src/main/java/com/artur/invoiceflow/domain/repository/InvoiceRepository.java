package com.artur.invoiceflow.domain.repository;
import com.artur.invoiceflow.domain.entity.Invoice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    boolean existsByNameIgnoreCase(String name);
}
