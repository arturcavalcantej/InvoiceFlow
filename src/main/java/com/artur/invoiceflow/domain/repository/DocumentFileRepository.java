package com.artur.invoiceflow.domain.repository;
import com.artur.invoiceflow.domain.entity.DocumentFile;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import com.artur.invoiceflow.domain.enums.DocumentType;

public interface DocumentFileRepository extends JpaRepository<DocumentFile, Long> {
    boolean existsByChecksumAndType(String checksum, DocumentType type);
    Page<DocumentFile> findByType(DocumentType type, Pageable pageable);
    Page<DocumentFile> findByChecksum(String checksum, Pageable pageable);
    Page<DocumentFile> findByTypeAndChecksum(DocumentType type, String checksum, Pageable pageable);
}
