package com.eli.ads.appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentResponse(
        Long id,
        LocalDate appointmentDate,
        LocalTime appointmentTime,
        String patientName,
        String dentistName,
        String surgeryName
) {}
