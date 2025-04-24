package com.eli.ads.billing;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByPatientId(Long patientId);
    boolean existsByPatientIdAndIsPaidFalse(Long patientId);
}
