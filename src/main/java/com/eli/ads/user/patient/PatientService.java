package com.eli.ads.user.patient;


import com.eli.ads.user.User;
import org.springframework.data.domain.Page;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface PatientService {
    Patient createPatient(Patient patient);
    PatientResponse registerPatient(PatientRequest patientRequest, User user) throws AccessDeniedException;
    List<PatientResponse> displayPatientsSortedByLastName(User user) throws AccessDeniedException;
    PatientResponse getPatientById(Long patientId, User user) throws AccessDeniedException;
    void deletePatient(Long patientId, User user) throws AccessDeniedException;
    List<PatientResponse> searchPatient(String searchString, User user) throws AccessDeniedException;
    PatientResponse updatePatient(Long patientId, PatientRequest patientRequest, User user) throws AccessDeniedException;
    Page<PatientResponse> getPatients(User user, int page, int size, String sortBy, String direction);
}
