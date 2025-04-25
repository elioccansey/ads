package com.eli.ads.user.patient;

import com.eli.ads.common.address.Address;
import com.eli.ads.user.User;
import com.eli.ads.user.UserService;
import com.eli.ads.user.auth.security.JwtService;
import com.eli.ads.user.auth.security.SecurityFilterChainConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityFilterChainConfig.class))
@AutoConfigureMockMvc(addFilters = false)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatientService patientService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private User mockUser;
    private PatientResponse samplePatient;
    private Address sampleAddress;

    @BeforeEach
    void setUp() {
        // Create mock user
        mockUser = new User();
        mockUser.setUsername("mock@user.com");

        // Create a simple response object
        samplePatient = new PatientResponse(
                1L,
                "John",
                "P001",
                "Doe",
                "1234567890",
                "john.doe@example.com",
                LocalDate.of(1980, 1, 1),
                null
        );

        // Initialize Address object (which was previously missing in PatientRequest)
        sampleAddress = new Address();
        sampleAddress.setStreet("123 Main St");
        sampleAddress.setCity("Springfield");
        sampleAddress.setState("IL");
        sampleAddress.setZipCode("62704");

        Mockito.when(userService.getConnectedUser()).thenReturn(mockUser);
    }

    @Test
    @WithMockUser(authorities = "LIST_PATIENTS")
    void whenGetPatients_thenReturnPagedResult() throws Exception {
        Mockito.when(patientService.getPatients(eq(mockUser), anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(new PageImpl<>(List.of(samplePatient), PageRequest.of(0, 10, Sort.by("lastName")), 1));

        mockMvc.perform(get("/patients")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "lastName")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].firstName").value("John"));
    }

    @Test
    @WithMockUser(authorities = "VIEW_PATIENT")
    void whenGetPatientById_thenReturnPatient() throws Exception {
        Mockito.when(patientService.getPatientById(eq(1L), eq(mockUser)))
                .thenReturn(samplePatient);

        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    @WithMockUser(authorities = "REGISTER_PATIENT")
    void whenPostCreatePatient_thenReturnCreated() throws Exception {
        // Include a valid address in the request
        PatientRequest request = new PatientRequest(
                null, "John", "P001", "Doe", "1234567890", "john.doe@example.com",
                LocalDate.of(1980, 1, 1), sampleAddress, null
        );

        Mockito.when(patientService.registerPatient(any(), eq(mockUser)))
                .thenReturn(samplePatient);

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    @WithMockUser(authorities = "UPDATE_PATIENT")
    void whenPutUpdatePatient_thenReturnUpdatedPatient() throws Exception {
        // Include a valid address in the update request
        PatientRequest request = new PatientRequest(
                1L, "John", "P001", "Doe", "1234567890", "john.doe@example.com",
                LocalDate.of(1980, 1, 1), sampleAddress, null
        );

        Mockito.when(patientService.updatePatient(eq(1L), any(), eq(mockUser)))
                .thenReturn(samplePatient);

        mockMvc.perform(put("/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(authorities = "DELETE_PATIENT")
    void whenDeletePatient_thenReturnNoContent() throws Exception {
        mockMvc.perform(delete("/patients/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(patientService).deletePatient(eq(1L), eq(mockUser));
    }

    @Test
    @WithMockUser(authorities = "SEARCH_PATIENT")
    void whenSearchPatients_thenReturnList() throws Exception {
        Mockito.when(patientService.searchPatient(eq("john"), eq(mockUser)))
                .thenReturn(List.of(samplePatient));

        mockMvc.perform(get("/patients/search")
                        .param("searchString", "john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }
}
