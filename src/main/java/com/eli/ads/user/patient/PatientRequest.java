package com.eli.ads.user.patient;

import com.eli.ads.common.address.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PatientRequest(
        Long id,
        @NotBlank(message = "First name is required.")
        String firstName,
        @NotBlank(message = "Patient number name is required.")
        String patientNumber,
        @NotBlank(message = "Last name is required.")
        String lastName,
        @NotBlank(message = "Phone number name is required.")
        String phoneNumber,
        @NotBlank(message = "Email name is required.")
        @Email(message = "Enter a valid email.")
        String email,
        @NotNull(message = "Date of birth is required.")
        LocalDate dateOfBirth,
        @NotNull(message = "Patient address is required.")
        Address address,
        Long userId
) {
}
