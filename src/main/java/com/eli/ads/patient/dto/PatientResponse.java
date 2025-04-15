package com.eli.ads.patient.dto;

import com.eli.ads.common.Address;

import java.time.LocalDate;

public record PatientResponse(
        Long id,
        String firstName,
        String patientNumber,
        String lastName,
        String phoneNumber,
        String email,
        LocalDate dateOfBirth,
        Address primaryAddress
){}
