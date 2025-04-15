package com.eli.ads.patient;

import com.eli.ads.patient.dto.PatientRequest;
import com.eli.ads.patient.dto.PatientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class PatientServiceImpl implements  PatientService{
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public PatientResponse savePatient(PatientRequest patientRequest) {
        return patientMapper.toPatientResponse(patientRepository.save(patientMapper.toPatient(patientRequest)));
    }

//    @Override
//    public List<Patient> displayPatientsSortedByLastName() {
//        return patientRepository.findAll(Sort.by("lastName"));
//    }
    @Override
    public List<PatientResponse> displayPatientsSortedByLastName() {
        return patientRepository.findAll(Sort.by("lastName"))
                .stream()
                .map(patientMapper::toPatientResponse)
                .toList();
    }
}
