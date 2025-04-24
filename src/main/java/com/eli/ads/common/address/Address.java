package com.eli.ads.common.address;

import com.eli.ads.user.patient.Patient;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "street", nullable = false)
    private String street;
    @Column(name = "city", nullable = false)
    private String city;
    @Column(name = "state")
    private String state;
    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "address")
    private Patient patient;



}
