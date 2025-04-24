package com.eli.ads.user.dentist.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DentistRequestDto(
        @NotBlank(message = "First name is required.") String firstName,
        @NotBlank(message = "Last name is required.") String lastName,
        @NotBlank(message = "Phone number is required.") String phoneNumber,
        @NotBlank(message = "Email is required.") @Email(message = "Enter a valid email.") String email,
        @NotBlank(message = "Specialization is required.") String specialization,
        Long userId
) {}
