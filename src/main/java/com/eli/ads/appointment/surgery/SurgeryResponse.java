package com.eli.ads.appointment.surgery;

import com.eli.ads.common.address.Address;

public record SurgeryResponse(
        Long id,
        String name,
        Address address,
        String phoneNumber
) {}
