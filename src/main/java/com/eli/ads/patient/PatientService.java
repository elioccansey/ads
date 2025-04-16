package com.eli.ads.patient;


import com.eli.ads.patient.dto.PatientRequest;
import com.eli.ads.patient.dto.PatientResponse;

import java.util.List;

public interface PatientService {
    Patient createPatient(Patient patient);
    PatientResponse savePatient(PatientRequest patient);
    List<PatientResponse> displayPatientsSortedByLastName();
    PatientResponse getPatientById(Long patientId);
    PatientResponse updatePatient(Long id, PatientRequest patientRequest);
    void deletePatient(Long patientId);
    List<PatientResponse> searchPatient(String searchString);
}
