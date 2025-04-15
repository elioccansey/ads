package com.eli.ads.patient;


import com.eli.ads.patient.dto.PatientRequest;
import com.eli.ads.patient.dto.PatientResponse;

import java.util.List;

public interface PatientService {
    Patient createPatient(Patient patient);
    PatientResponse savePatient(PatientRequest patient);
//    List<Patient> displayPatientsSortedByLastName();
    List<PatientResponse> displayPatientsSortedByLastName();
}
