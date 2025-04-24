package com.eli.ads.appointment;

import com.eli.ads.billing.Bill;
import com.eli.ads.appointment.surgery.Surgery;
import com.eli.ads.user.dentist.Dentist;
import com.eli.ads.user.patient.Patient;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Column(nullable = false)
    private LocalTime appointmentTime;

    @ManyToOne()
    private Patient patient;

    @ManyToOne()
    @JoinColumn(nullable = true)
    private Dentist dentist;

    @ManyToOne
    private Surgery surgery;

    @OneToOne
    private Bill bill;

}
