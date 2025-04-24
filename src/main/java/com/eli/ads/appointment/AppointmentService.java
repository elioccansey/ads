package com.eli.ads.appointment;

import com.eli.ads.appointment.dto.AppointmentRequest;
import com.eli.ads.appointment.dto.AppointmentResponse;
import com.eli.ads.user.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AppointmentService {

    List<AppointmentResponse> getAppointmentsByPatientId(Long patientId, User user);
    List<AppointmentResponse> getAppointmentsByDentistId(Long dentistId, User user);

    AppointmentResponse bookAppointment(AppointmentRequest appointmentRequest, User user) throws AccessDeniedException;

    void cancelAppointment(Long id, User user) throws AccessDeniedException, AccessDeniedException;

    AppointmentResponse rescheduleAppointment(Long id, AppointmentRequest appointmentRequest, User user) throws AccessDeniedException;

    @Transactional
    void removeDentistFromAppointments(Long dentistId);
}
