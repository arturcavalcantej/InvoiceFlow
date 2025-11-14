package com.artur.invoiceflow.domain.repository;
import com.artur.invoiceflow.domain.entity.InvoiceItem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {
    boolean existsByNameIgnoreCase(String name);
}
