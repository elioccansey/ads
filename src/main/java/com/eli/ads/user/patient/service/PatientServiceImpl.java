package com.eli.ads.user.patient.service;

import com.eli.ads.common.exception.ResourceNotFoundException;
import com.eli.ads.user.User;
import com.eli.ads.user.UserService;
import com.eli.ads.user.auth.RegistrationRequest;
import com.eli.ads.user.patient.Patient;
import com.eli.ads.user.patient.PatientMapper;
import com.eli.ads.user.patient.PatientRepository;
import com.eli.ads.user.patient.PatientService;
import com.eli.ads.user.patient.PatientRequest;
import com.eli.ads.user.patient.PatientResponse;
import com.eli.ads.user.permission.PermissionEnum;
import com.eli.ads.user.role.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final UserService userService;


    @Override
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public PatientResponse registerPatient(PatientRequest patientRequest, User user) {
        user.checkPermission(PermissionEnum.REGISTER_PATIENT);
        Patient patient = patientMapper.toPatient(patientRequest);
        Long userId = patientRequest.userId();
        if(userId != null){
            User foundUser = userService.findUserById(userId);
            patient.setUser(foundUser);
        }else{
            patient.setUser(userService.saveUser(
                    new RegistrationRequest(
                            patientRequest.email(),
                            //TODO Generate Secure password
                            "defaultPassword",
                            Set.of(RoleEnum.PATIENT.name())
                    )
            ));
        }
        return patientMapper.toPatientResponse(patientRepository.save(patient));
    }


    @Override
    public List<PatientResponse> displayPatientsSortedByLastName(User user) {
        user.checkPermission(PermissionEnum.LIST_PATIENTS);
        return patientRepository.findAll(Sort.by("lastName"))
                .stream()
                .map(patientMapper::toPatientResponse)
                .toList();
    }

    @Override
    public PatientResponse getPatientById(Long patientId, User user) {
        user.checkPermission(PermissionEnum.VIEW_PATIENT);
        return patientMapper.toPatientResponse(findPatientById(patientId));
    }

    @Override
    public PatientResponse updatePatient(Long id, PatientRequest patientRequest, User user) {
        user.checkPermission(PermissionEnum.UPDATE_PATIENT);
        var existingPatient = findPatientById(id);
        var updatedPatient = patientMapper.toPatient(patientRequest);
        updatedPatient.setId(existingPatient.getId());
        return patientMapper.toPatientResponse(patientRepository.save(updatedPatient));
    }

    @Override
    public Page<PatientResponse> getPatients(User user, int page, int size, String sortBy, String direction) {
        user.checkPermission(PermissionEnum.LIST_PATIENTS);
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        // Assuming your repository returns Patient entities:
        Page<Patient> patientPage = patientRepository.findAll(pageable);

        // Map to DTO (PatientResponse)
        return patientPage.map(patientMapper::toPatientResponse);
    }


    @Override
    public void deletePatient(Long patientId, User user) {
        user.checkPermission(PermissionEnum.DELETE_PATIENT);
        findPatientById(patientId);
        patientRepository.deleteById(patientId);
    }

    @Override
    public List<PatientResponse> searchPatient(String searchString, User user) throws AccessDeniedException {
        user.checkPermission(PermissionEnum.SEARCH_PATIENT);
        String formattedSearchString = "%" + searchString.toLowerCase() + "%";
        return patientRepository.searchPatient(formattedSearchString)
                .stream()
                .map(patientMapper::toPatientResponse)
                .toList();
    }

    private Patient findPatientById(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with Id " + patientId));
    }
}
