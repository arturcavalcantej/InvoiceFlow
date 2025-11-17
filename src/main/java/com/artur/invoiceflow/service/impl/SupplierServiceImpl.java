package com.artur.invoiceflow.service.impl;

import com.artur.invoiceflow.domain.entity.Supplier;
import com.artur.invoiceflow.domain.repository.SupplierRepository;
import com.artur.invoiceflow.dto.supplier.SupplierRequest;
import com.artur.invoiceflow.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.artur.invoiceflow.dto.supplier.SupplierResponse;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository sup;

    @Transactional(readOnly = true)
    public Page<SupplierResponse> list(String cnpjOrName, Pageable pageable) {

        if (cnpjOrName != null && !cnpjOrName.isEmpty()) {
            return sup.findByCnpj(cnpjOrName, pageable).map(this::toResponse);
        }
        return sup.findAll(pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponse get(Long id) {
        return sup.findById(id).map(this::toResponse).orElseThrow();
    }

    @Override
    public void delete(Long id) {
        sup.deleteById(id);
    }

    @Transactional
    @Override
    public SupplierResponse create(SupplierRequest body) {
        requireNonNull(body);

        var supplier = Supplier.builder()
                .cnpj(body.cnpj())
                .name(body.name())
                .phone(body.phone())
                .email(body.email())
                .build();

        var savedSupplier = sup.save(supplier);
        return toResponse(savedSupplier);
    }

    @Transactional
    @Override
    public SupplierResponse update(Long id, SupplierRequest body) {
        requireNonNull(body);
        var supplier = sup.findById(id).orElseThrow();
        supplier.setName(body.name());
        supplier.setPhone(body.phone());
        supplier.setEmail(body.email());
        supplier.setCnpj(body.cnpj());
        return toResponse(sup.save(supplier));
    }

    private SupplierResponse toResponse(Supplier sup){
    return new SupplierResponse(sup.getId(),
            sup.getCnpj(),
            sup.getName(),
            sup.getEmail(),
            sup.getEmail(),
            sup.getCreatedAt(),
            sup.getUpdatedAt()
    );
    }

}
