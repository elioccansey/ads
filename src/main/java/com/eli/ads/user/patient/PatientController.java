package com.eli.ads.user.patient;

import com.eli.ads.user.UserService;
import com.eli.ads.user.User;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
class PatientController {

    private final PatientService patientService;
    private final UserService userService;

    // View all patients - only accessible to users with LIST_PATIENTS authority
    @PreAuthorize("hasAuthority('LIST_PATIENTS')")
    @GetMapping
    public ResponseEntity<List<PatientResponse>> displayPatientsSortedByLastName() throws AccessDeniedException {
        User user = userService.getConnectedUser();
        return ResponseEntity.ok(patientService.displayPatientsSortedByLastName(user));
    }

    // Get specific patient by ID - only accessible to users with VIEW_PATIENT authority
    @PreAuthorize("hasAuthority('VIEW_PATIENT')")
    @GetMapping("/{patient-id}")
    @ResponseStatus(HttpStatus.OK)
    public PatientResponse getPatientById(@PathVariable("patient-id") Long patientId) throws AccessDeniedException {
        User user = userService.getConnectedUser();
        return patientService.getPatientById(patientId, user);
    }

    // Create new patient - only accessible to users with REGISTER_PATIENT authority
    @PreAuthorize("hasAuthority('REGISTER_PATIENT')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientResponse createPatient(@Valid @RequestBody PatientRequest patientRequest) throws AccessDeniedException {
        User user = userService.getConnectedUser();
        return patientService.registerPatient(patientRequest, user);
    }

    // Update patient information - only accessible to users with UPDATE_PATIENT authority
    @PreAuthorize("hasAuthority('UPDATE_PATIENT')")
    @PutMapping("/{patient-id}")
    @ResponseStatus(HttpStatus.OK)
    public PatientResponse updatePatient(
            @PathVariable("patient-id") Long patientId,
            @Valid @RequestBody PatientRequest patientRequest) throws AccessDeniedException {
        User user = userService.getConnectedUser();
        return patientService.updatePatient(patientId, patientRequest, user);
    }

    // Delete patient - only accessible to users with DELETE_PATIENT authority
    @PreAuthorize("hasAuthority('DELETE_PATIENT')")
    @DeleteMapping("/{patient-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable("patient-id") Long patientId) throws AccessDeniedException {
        User user = userService.getConnectedUser();
        patientService.deletePatient(patientId, user);
    }

    // Search patients by search string - only accessible to users with SEARCH_PATIENT authority
    @PreAuthorize("hasAuthority('SEARCH_PATIENT')")
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<PatientResponse> searchPatient(@RequestParam("searchString") String searchString) throws AccessDeniedException {
        User user = userService.getConnectedUser();
        return patientService.searchPatient(searchString, user);
    }
}
