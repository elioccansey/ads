package com.eli.ads.appointment.surgery;

import com.eli.ads.common.address.Address;

public record SurgeryRequest(
        String name,
        Address address,
        String phoneNumber
) {}
