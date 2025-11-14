package com.artur.invoiceflow.dto.supplier;

import jakarta.validation.constraints.*;

public record SupplierRequest(
        @NotBlank @Size(max = 18)
        String cnpj,
        @NotBlank @Size(max = 160)
        String name,
        @Email @Size(max = 160)
        String email,
        @Size(max = 40)
        String phone
) {}