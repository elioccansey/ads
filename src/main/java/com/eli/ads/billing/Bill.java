package com.eli.ads.billing;

import com.eli.ads.appointment.Appointment;
import com.eli.ads.user.patient.Patient;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "bills")
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
