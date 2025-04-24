package com.eli.ads.user.dentist.dto;


public record DentistResponseDto(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String specialization
) {}
