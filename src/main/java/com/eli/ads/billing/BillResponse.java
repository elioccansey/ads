package com.eli.ads.billing;

public record BillResponse(
        Long id,
        double amount,
        boolean isPaid
) {}
