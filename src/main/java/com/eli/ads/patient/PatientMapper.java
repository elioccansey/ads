package com.eli.ads.patient;

import com.eli.ads.patient.dto.PatientRequest;
import com.eli.ads.patient.dto.PatientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    Patient toPatient(PatientRequest patientRequest);
    @Mapping(source = "address", target = "primaryAddress")
    PatientResponse toPatientResponse(Patient patient);

}


