package com.eli.ads.appointment;

import com.eli.ads.appointment.dto.AppointmentRequest;
import com.eli.ads.appointment.dto.AppointmentResponse;
import com.eli.ads.user.User;
import com.eli.ads.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserService userService;

    // Book Appointment (requires 'BOOK_APPOINTMENT' permission)
    @PreAuthorize("hasAuthority('BOOK_APPOINTMENT')")
    @PostMapping
    public ResponseEntity<AppointmentResponse> bookAppointment(@Valid @RequestBody AppointmentRequest dto) throws AccessDeniedException {
        User user = userService.getConnectedUser();
        return ResponseEntity.ok(appointmentService.bookAppointment(dto, user));
    }

    @PreAuthorize("hasAuthority('VIEW_APPOINTMENT')")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByPatientId(@PathVariable Long patientId) {
        User user = userService.getConnectedUser();
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatientId(patientId, user));
    }

    @PreAuthorize("hasAuthority('VIEW_APPOINTMENT')")
    @GetMapping("/dentist/{dentistId}")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByDentistId(@PathVariable Long dentistId) {
        User user = userService.getConnectedUser();
        return ResponseEntity.ok(appointmentService.getAppointmentsByDentistId(dentistId, user));
    }

    @PreAuthorize("hasAuthority('RESCHEDULE_APPOINTMENT')")
    @PutMapping("/{id}/reschedule")
    public ResponseEntity<AppointmentResponse> rescheduleAppointment(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentRequest request
    ) throws AccessDeniedException {
        User user = userService.getConnectedUser();
        return ResponseEntity.ok(
                appointmentService.rescheduleAppointment(id, request, user)
        );
    }

    // Cancel Appointment (requires 'CANCEL_APPOINTMENT' permission)
    @PreAuthorize("hasAuthority('CANCEL_APPOINTMENT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id) throws AccessDeniedException {
        User user = userService.getConnectedUser();
        appointmentService.cancelAppointment(id, user);
        return ResponseEntity.noContent().build();
    }



}
