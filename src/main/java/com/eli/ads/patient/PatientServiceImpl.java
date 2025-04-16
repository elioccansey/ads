package com.eli.ads.patient;

import com.eli.ads.patient.dto.PatientRequest;
import com.eli.ads.patient.dto.PatientResponse;
import jakarta.persistence.EntityNotFoundException;
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

    @Override
    public List<PatientResponse> displayPatientsSortedByLastName() {
        return patientRepository.findAll(Sort.by("lastName"))
                .stream()
                .map(patientMapper::toPatientResponse)
                .toList();
    }

    @Override
    public PatientResponse getPatientById(Long patientId) {
        return patientMapper.toPatientResponse(this.findPatientById(patientId));
    }


    public PatientResponse updatePatient(Long id, PatientRequest patientRequest){
        var existingPatient = this.getPatientById(id);
        var updatedPatient = patientMapper.toPatient(patientRequest);
        updatedPatient.setId(existingPatient.id());
        return patientMapper.toPatientResponse(patientRepository.save(updatedPatient));

    }

    @Override
    public void deletePatient(Long patientId) {
        this.findPatientById(patientId);
        patientRepository.deleteById(patientId);
    }

    @Override
    public List<PatientResponse> searchPatient(String searchString) {
        String formattedSearchString  ="%"+searchString+"%".toLowerCase();
        return patientRepository
                .searchPatient(formattedSearchString)
                .stream()
                .map(patientMapper::toPatientResponse)
                .toList();
    }

    private Patient findPatientById(Long patientId){
        return patientRepository
                .findById(patientId)
                .orElseThrow(()->new EntityNotFoundException("Patient not found with Id " + patientId));
    }
}
