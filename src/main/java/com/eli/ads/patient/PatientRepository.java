package com.eli.ads.patient;

import com.eli.ads.patient.dto.PatientResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("""
    SELECT p FROM Patient p
    LEFT JOIN p.address a
    WHERE LOWER(p.firstName) LIKE :searchString
       OR LOWER(p.lastName) LIKE :searchString
       OR LOWER(p.email) LIKE :searchString
       OR LOWER(p.patientNumber) LIKE :searchString
       OR LOWER(p.phoneNumber) LIKE :searchString
       OR LOWER(a.street) LIKE :searchString
       OR LOWER(a.city) LIKE :searchString
       OR LOWER(a.state) LIKE :searchString
       OR LOWER(a.zipCode) LIKE :searchString
""")
    List<Patient> searchPatient(@Param("searchString") String searchString);
}
