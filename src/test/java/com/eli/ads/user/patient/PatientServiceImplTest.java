package com.eli.ads.user.patient;

import com.eli.ads.common.address.Address;
import com.eli.ads.common.address.AddressResponse;
import com.eli.ads.common.exception.ResourceNotFoundException;
import com.eli.ads.user.User;
import com.eli.ads.user.UserService;
import com.eli.ads.user.auth.RegistrationRequest;
import com.eli.ads.user.patient.service.PatientServiceImpl;
import com.eli.ads.user.permission.PermissionEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @Mock
    private UserService userService;

    @Mock
    private User user;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient patient;
    private PatientRequest patientRequest;
    private PatientResponse patientResponse;
    private Address address;
    private AddressResponse addressResponse;

    private final Long patientId = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        address = new Address();
        address.setId(100L);
        address.setStreet("123 Main St");
        address.setCity("Springfield");
        address.setState("IL");
        address.setZipCode("62704");

        patientRequest = new PatientRequest(
                null,
                "Alice",
                "P-123",
                "Smith",
                "1234567890",
                "alice@example.com",
                LocalDate.of(1990, 1, 1),
                address,
                null
        );

        patient = new Patient();
        patient.setId(patientId);
        patient.setFirstName("Alice");
        patient.setPatientNumber("P-123");
        patient.setLastName("Smith");
        patient.setPhoneNumber("1234567890");
        patient.setEmail("alice@example.com");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setAddress(address);

        addressResponse = new AddressResponse(
                address.getId(),
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getZipCode(),
                null
        );

        patientResponse = new PatientResponse(
                patientId,
                "Alice",
                "P-123",
                "Smith",
                "1234567890",
                "alice@example.com",
                LocalDate.of(1990, 1, 1),
                addressResponse
        );
    }

    @Test
    void givenValidRequest_whenRegisterPatient_thenReturnsPatientResponse() {
        // Given
        doNothing().when(user).checkPermission(PermissionEnum.REGISTER_PATIENT);
        when(patientMapper.toPatient(patientRequest)).thenReturn(patient);
        when(userService.saveUser(any(RegistrationRequest.class))).thenReturn(user);
        patient.setUser(user);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.toPatientResponse(patient)).thenReturn(patientResponse);

        // When
        PatientResponse result = patientService.registerPatient(patientRequest, user);

        // Then
        assertNotNull(result);
        assertEquals("Alice", result.firstName());
        assertEquals("Springfield", result.primaryAddress().getCity());
        verify(userService).saveUser(any(RegistrationRequest.class));
        verify(patientRepository).save(patient);
    }

    @Test
    void givenExistingPatientId_whenGetPatientById_thenReturnsResponse() {
        // Given
        doNothing().when(user).checkPermission(PermissionEnum.VIEW_PATIENT);
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(patientMapper.toPatientResponse(patient)).thenReturn(patientResponse);

        // When
        PatientResponse result = patientService.getPatientById(patientId, user);

        // Then
        assertNotNull(result);
        assertEquals("Alice", result.firstName());
    }

    @Test
    void givenNonExistingPatientId_whenGetPatientById_thenThrowsException() {
        // Given
        doNothing().when(user).checkPermission(PermissionEnum.VIEW_PATIENT);
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> patientService.getPatientById(patientId, user));
    }

    @Test
    void givenValidUpdateRequest_whenUpdatePatient_thenReturnsUpdatedResponse() {
        // Given
        doNothing().when(user).checkPermission(PermissionEnum.UPDATE_PATIENT);
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(patientMapper.toPatient(patientRequest)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.toPatientResponse(patient)).thenReturn(patientResponse);

        // When
        PatientResponse result = patientService.updatePatient(patientId, patientRequest, user);

        // Then
        assertNotNull(result);
        assertEquals("Alice", result.firstName());
        verify(patientRepository).save(patient);
    }

    @Test
    void givenSortRequest_whenDisplaySortedPatients_thenReturnListSortedByLastName() {
        // Given
        doNothing().when(user).checkPermission(PermissionEnum.LIST_PATIENTS);
        when(patientRepository.findAll(Sort.by("lastName"))).thenReturn(List.of(patient));
        when(patientMapper.toPatientResponse(patient)).thenReturn(patientResponse);

        // When
        List<PatientResponse> result = patientService.displayPatientsSortedByLastName(user);

        // Then
        assertEquals(1, result.size());
        assertEquals("Smith", result.get(0).lastName());
    }

    @Test
    void givenPageRequest_whenGetPatients_thenReturnPaginatedList() {
        // Given
        doNothing().when(user).checkPermission(PermissionEnum.LIST_PATIENTS);
        Pageable pageable = PageRequest.of(0, 1, Sort.by("lastName").ascending());
        Page<Patient> page = new PageImpl<>(List.of(patient));
        when(patientRepository.findAll(pageable)).thenReturn(page);
        when(patientMapper.toPatientResponse(patient)).thenReturn(patientResponse);

        // When
        Page<PatientResponse> result = patientService.getPatients(user, 0, 1, "lastName", "asc");

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals("Alice", result.getContent().get(0).firstName());
    }

    @Test
    void givenValidId_whenDeletePatient_thenRepositoryDeleteIsCalled() {
        // Given
        doNothing().when(user).checkPermission(PermissionEnum.DELETE_PATIENT);
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        // When
        patientService.deletePatient(patientId, user);

        // Then
        verify(patientRepository).deleteById(patientId);
    }

    @Test
    void givenSearchTerm_whenSearchPatient_thenReturnFilteredList() throws Exception {
        // Given
        doNothing().when(user).checkPermission(PermissionEnum.SEARCH_PATIENT);
        String term = "alice";
        when(patientRepository.searchPatient("%alice%")).thenReturn(List.of(patient));
        when(patientMapper.toPatientResponse(patient)).thenReturn(patientResponse);

        // When
        List<PatientResponse> result = patientService.searchPatient(term, user);

        // Then
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).firstName());
    }
}
