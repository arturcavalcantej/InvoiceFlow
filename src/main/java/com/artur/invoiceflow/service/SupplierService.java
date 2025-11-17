package com.artur.invoiceflow.service;

import com.artur.invoiceflow.dto.supplier.SupplierRequest;
import com.artur.invoiceflow.dto.supplier.SupplierResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SupplierService {
    SupplierResponse create(SupplierRequest body);
    SupplierResponse update(Long id, SupplierRequest body);
    SupplierResponse get(Long id);
    Page<SupplierResponse> list(String cnpjOrName, Pageable pageable);
    void delete(Long id);
}
