package com.artur.invoiceflow.dto.document;

import jakarta.validation.constraints.*;

public record DocumentFileRequest {
    @NotBlank @Size(max=120) String name,

}

