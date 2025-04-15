package com.eli.ads.dentist;

import org.springframework.data.jpa.repository.JpaRepository;

interface DentistRepository extends JpaRepository<Dentist, Long> {
}
