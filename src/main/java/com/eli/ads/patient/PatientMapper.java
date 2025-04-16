package com.eli.ads.patient;

import com.eli.ads.common.address.AddressResponse;
import com.eli.ads.patient.dto.PatientRequest;
import com.eli.ads.patient.dto.PatientResponse;

public class PatientMapper {

    public Patient toPatient(PatientRequest request) {
        if (request == null) return null;

        Patient patient = new Patient();
        patient.setId(request.id());
        patient.setFirstName(request.firstName());
        patient.setPatientNumber(request.patientNumber());
        patient.setLastName(request.lastName());
        patient.setPhoneNumber(request.phoneNumber());
        patient.setEmail(request.email());
        patient.setDateOfBirth(request.dateOfBirth());
        patient.setAddress(request.address());
        patient.setAppointments(request.appointments());
        patient.setUser(request.user());

        return patient;
    }

    public PatientResponse toPatientResponse(Patient patient) {
        if (patient == null) return null;

        return new PatientResponse(
                patient.getId(),
                patient.getFirstName(),
                patient.getPatientNumber(),
                patient.getLastName(),
                patient.getPhoneNumber(),
                patient.getEmail(),
                patient.getDateOfBirth(),
               new AddressResponse(
                       patient.getAddress().getId(),
                       patient.getAddress().getStreet(),
                       patient.getAddress().getCity(),
                       patient.getAddress().getState(),
                       patient.getAddress().getZipCode(),
                       null
               )
        );
    }
}
