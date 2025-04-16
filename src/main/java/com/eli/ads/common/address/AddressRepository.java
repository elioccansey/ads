package com.eli.ads.common.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("""
    SELECT a
    FROM Address a
    JOIN FETCH Patient p on p.id = a.patient.id
    ORDER BY a.city 
    """)
    List<Address> getAllAddressesSortedByCity();
}
