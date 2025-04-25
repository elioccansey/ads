package com.eli.ads.user.patient;

import com.eli.ads.common.address.Address;
import com.eli.ads.user.User;
import com.eli.ads.user.permission.Permission;
import com.eli.ads.user.permission.PermissionEnum;
import com.eli.ads.user.role.Role;
import com.eli.ads.user.role.RoleEnum;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private EntityManager entityManager;

    private Patient patient;

    @BeforeEach
    void setUp() {
        // === Create Permission ===
        Permission viewPermission = new Permission();
        viewPermission.setName(PermissionEnum.VIEW_PATIENT);
        entityManager.persist(viewPermission);

        // === Create Role ===
        Role role = new Role();
        role.setName(RoleEnum.PATIENT);
        role.setPermissions(Set.of(viewPermission));
        entityManager.persist(role);

        // === Create User ===
        User user = new User();
        user.setUsername("alice@example.com");
        user.setPassword("securepass123");
        user.setRoles(Set.of(role));
        entityManager.persist(user);

        // === Create Address ===
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("Springfield");
        address.setState("IL");
        address.setZipCode("62704");
        entityManager.persist(address);

        // === Create Patient ===
        patient = new Patient();
        patient.setFirstName("Alice");
        patient.setLastName("Smith");
        patient.setEmail("alice@example.com");
        patient.setPatientNumber("P123");
        patient.setPhoneNumber("1234567890");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setAddress(address);
        patient.setUser(user);

        entityManager.persist(patient);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void whenFindById_thenPatientIncludesUserRoleAndPermission() {
        // When
        Optional<Patient> found = patientRepository.findById(patient.getId());

        // Then
        assertThat(found).isPresent();
        Patient p = found.get();

        assertThat(p.getFirstName()).isEqualTo("Alice");
        assertThat(p.getAddress()).isNotNull();
        assertThat(p.getUser()).isNotNull();

        User user = p.getUser();
        assertThat(user.getUsername()).isEqualTo("alice@example.com");
        assertThat(user.getRoles()).isNotEmpty();

        Role role = user.getRoles().iterator().next();
        assertThat(role.getName()).isEqualTo(RoleEnum.PATIENT);
        assertThat(role.getPermissions()).anyMatch(perm -> perm.getName() == PermissionEnum.VIEW_PATIENT);
    }

    @Test
    void whenSearchPatientByEmail_thenReturnsPatient() {
        // When
        List<Patient> results = patientRepository.searchPatient("%alice@example.com%");

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void whenDeletePatient_thenEntityIsRemoved() {
        // When
        patientRepository.deleteById(patient.getId());
        entityManager.flush();

        // Then
        assertThat(patientRepository.findById(patient.getId())).isEmpty();
    }
}
