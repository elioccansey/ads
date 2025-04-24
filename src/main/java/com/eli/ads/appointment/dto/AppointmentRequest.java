// AppointmentRequest.java
package com.eli.ads.appointment.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentRequest(
        @NotNull @Future LocalDate appointmentDate,
        @NotNull LocalTime appointmentTime,
        @NotNull Long patientId,
        @NotNull Long dentistId,
        @NotNull Long surgeryId
) {}
