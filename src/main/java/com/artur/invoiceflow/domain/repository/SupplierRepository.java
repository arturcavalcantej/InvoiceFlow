package com.artur.invoiceflow.domain.repository;
import com.artur.invoiceflow.domain.entity.Supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    boolean existsByNameIgnoreCase(String name);
    Page<Supplier> findByCnpj(String cnpj, Pageable pageable);
}
