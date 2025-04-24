package com.eli.ads.appointment;

import com.eli.ads.user.patient.Patient;
import com.eli.ads.user.dentist.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatient(Patient patient);
    List<Appointment> findByDentist(Dentist dentist);
    long countByDentistIdAndAppointmentDateBetween(Long dentistId , LocalDate weekStart, LocalDate weekEnd);
    List<Appointment> findByDentistId(Long dentistId);
}
