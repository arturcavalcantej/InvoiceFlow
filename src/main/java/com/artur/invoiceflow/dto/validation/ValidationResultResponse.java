package com.artur.invoiceflow.dto.validation;

import java.time.OffsetDateTime;
import java.util.Map;

public record ValidationResultResponse(
        Long id,
        Long invoiceId,
        Map<String, String> rulesPassed,        // ex: {"CFOP":"ok","TOTAL_MATCH":"ok"}
        Map<String, String> rulesFailed,        // ex: {"CNPJ":"inválido"}
        Integer score,                          // 0-100
        OffsetDateTime validatedAt
) {}
