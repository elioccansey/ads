package com.eli.ads.patient.dto;

import com.eli.ads.appointment.Appointment;
import com.eli.ads.common.address.Address;
import com.eli.ads.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

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
        Address address,
        List<Appointment> appointments,
        User user
) {
}
