package com.artur.invoiceflow.domain.repository;
import com.artur.invoiceflow.domain.entity.DocumentFile;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentFileRepository extends JpaRepository<DocumentFile, Long> {
    boolean existsByNameIgnoreCase(String name);
}
