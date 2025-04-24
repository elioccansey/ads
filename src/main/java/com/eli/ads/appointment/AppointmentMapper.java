package com.eli.ads.appointment;

import com.eli.ads.appointment.dto.AppointmentRequest;
import com.eli.ads.appointment.dto.AppointmentResponse;
import com.eli.ads.appointment.surgery.Surgery;
import com.eli.ads.user.dentist.Dentist;
import com.eli.ads.user.patient.Patient;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    // Convert AppointmentRequest DTO to Appointment entity
    public Appointment toAppointment(AppointmentRequest appointmentRequest, Dentist dentist, Patient patient, Surgery surgery) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(appointmentRequest.appointmentDate());
        appointment.setAppointmentTime(appointmentRequest.appointmentTime());
        appointment.setDentist(dentist);
        appointment.setPatient(patient);
        appointment.setSurgery(surgery);
        return appointment;
    }

    // Convert Appointment entity to AppointmentResponse DTO
    public AppointmentResponse toAppointmentResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                appointment.getDentist().getFirstName() + " " + appointment.getDentist().getLastName(),
                appointment.getSurgery().getName(),
                appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName()
        );
    }

    // Update Appointment entity from AppointmentRequest DTO
    public void updateAppointmentFromRequest(AppointmentRequest appointmentRequest, Appointment appointment) {
        appointment.setAppointmentDate(appointmentRequest.appointmentDate());
        appointment.setAppointmentTime(appointmentRequest.appointmentTime());
    }
}
