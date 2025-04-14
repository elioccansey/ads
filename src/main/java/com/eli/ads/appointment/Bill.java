package com.eli.ads.appointment;

import com.eli.ads.patient.Patient;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal amount;
    private boolean isPaid;

    @OneToOne()
    private Appointment appointment;

    @OneToOne
    private Patient patient;
}
