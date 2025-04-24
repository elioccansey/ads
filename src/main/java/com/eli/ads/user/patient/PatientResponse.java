package com.eli.ads.user.patient;

import com.eli.ads.common.address.AddressResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PatientResponse(
        Long id,
        String firstName,
        String patientNumber,
        String lastName,
        String phoneNumber,
        String email,
        LocalDate dateOfBirth,
        AddressResponse primaryAddress
){}
