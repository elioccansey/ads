package com.eli.ads.appointment;

import com.eli.ads.appointment.dto.AppointmentRequest;
import com.eli.ads.appointment.dto.AppointmentResponse;
import com.eli.ads.appointment.surgery.Surgery;
import com.eli.ads.appointment.surgery.SurgeryRepository;
import com.eli.ads.appointment.utils.DateUtils;
import com.eli.ads.billing.BillRepository;
import com.eli.ads.user.dentist.Dentist;
import com.eli.ads.user.dentist.DentistRepository;
import com.eli.ads.user.patient.Patient;
import com.eli.ads.user.patient.PatientRepository;
import com.eli.ads.user.User;
import com.eli.ads.user.permission.PermissionEnum;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DentistRepository dentistRepository;
    private final AppointmentMapper appointmentMapper;
    private final SurgeryRepository surgeryRepository;
    private final BillRepository billRepository;

    @Override
    public AppointmentResponse bookAppointment(AppointmentRequest appointmentRequest, User user) {
        user.checkPermission(PermissionEnum.BOOK_APPOINTMENT);

        Patient patient = patientRepository.findById(appointmentRequest.patientId())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        Dentist dentist = dentistRepository.findById(appointmentRequest.dentistId())
                .orElseThrow(() -> new EntityNotFoundException("Dentist not found"));

        Surgery surgery = surgeryRepository.findById(appointmentRequest.surgeryId())
                .orElseThrow(() -> new EntityNotFoundException("Surgery not found"));

        if (hasUnpaidBill(patient)) {
            throw new RuntimeException("Patient has an unpaid bill, cannot book appointment.");
        }

        var weekStart = DateUtils.getWeekStart(appointmentRequest.appointmentDate());
        var weekEnd = DateUtils.getWeekEnd(appointmentRequest.appointmentDate());
        long appointmentCount = appointmentRepository.countByDentistIdAndAppointmentDateBetween(
                appointmentRequest.dentistId(), weekStart, weekEnd
        );

        if (appointmentCount >= 5) {
            throw new ValidationException("Dentist has already 5 appointments this week.");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDentist(dentist);
        appointment.setSurgery(surgery);
        appointment.setAppointmentDate(appointmentRequest.appointmentDate());
        appointment.setAppointmentTime(appointmentRequest.appointmentTime());

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toAppointmentResponse(savedAppointment);
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByPatientId(Long patientId, User user) {
        user.checkPermission(PermissionEnum.VIEW_APPOINTMENT);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        if (!patient.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to view this patient's appointments.");
        }

        return appointmentRepository.findByPatient(patient).stream()
                .map(appointmentMapper::toAppointmentResponse)
                .toList();
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByDentistId(Long dentistId, User user) {
        user.checkPermission(PermissionEnum.VIEW_APPOINTMENT);

        Dentist dentist = dentistRepository.findById(dentistId)
                .orElseThrow(() -> new RuntimeException("Dentist not found"));

        if (!dentist.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to view this dentist's appointments.");
        }

        return appointmentRepository.findByDentist(dentist).stream()
                .map(appointmentMapper::toAppointmentResponse)
                .toList();
    }

    @Override
    public void cancelAppointment(Long id, User user) throws AccessDeniedException {
        user.checkPermission(PermissionEnum.CANCEL_APPOINTMENT);

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        boolean isPatient = appointment.getPatient().getUser().getId().equals(user.getId());
        boolean isDentist = appointment.getDentist().getUser().getId().equals(user.getId());

        if (!isPatient && !isDentist) {
            throw new AccessDeniedException("You do not have permission to cancel this appointment.");
        }

        appointmentRepository.delete(appointment);
    }

    @Override
    public AppointmentResponse rescheduleAppointment(Long id, AppointmentRequest appointmentRequest, User user) throws AccessDeniedException {
        user.checkPermission(PermissionEnum.RESCHEDULE_APPOINTMENT);

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        boolean isPatient = appointment.getPatient().getUser().getId().equals(user.getId());
        boolean isDentist = appointment.getDentist().getUser().getId().equals(user.getId());

        if (!isPatient && !isDentist) {
            throw new AccessDeniedException("You do not have permission to reschedule this appointment.");
        }

        appointment.setAppointmentDate(appointmentRequest.appointmentDate());
        appointment.setAppointmentTime(appointmentRequest.appointmentTime());
        Appointment updated = appointmentRepository.save(appointment);

        return appointmentMapper.toAppointmentResponse(updated);
    }
    @Override
    @Transactional
    public void removeDentistFromAppointments(Long dentistId) {
        List<Appointment> appointments = appointmentRepository.findByDentistId(dentistId);
        for (Appointment appointment : appointments) {
            appointment.setDentist(null);
        }
        appointmentRepository.saveAll(appointments);
    }

    private boolean hasUnpaidBill(Patient patient) {
        return billRepository.existsByPatientIdAndIsPaidFalse(patient.getId());
    }
}
