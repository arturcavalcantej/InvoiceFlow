package com.artur.invoiceflow.domain.repository;
import com.artur.invoiceflow.domain.entity.Invoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InvoiceRepository extends JpaRepository<Invoice, Long>,
        JpaSpecificationExecutor<Invoice> {
    boolean existsByNumberIgnoreCase(String number);
}
