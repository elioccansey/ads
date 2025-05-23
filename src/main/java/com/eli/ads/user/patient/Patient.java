package com.eli.ads.user.patient;

import com.eli.ads.appointment.Appointment;
import com.eli.ads.common.address.Address;
import com.eli.ads.user.User;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "First name is required.")
    private String firstName;
    @NotBlank(message = "Patient number name is required.")
    private String patientNumber;
    @NotBlank(message = "Last name is required.")
    private String lastName;
    @NotBlank(message = "Phone number name is required.")
    private String phoneNumber;
    @NotBlank(message = "Email name is required.")
    @Email(message = "Enter a valid email.")
    private String email;
    @NotNull(message = "Date of birth is required.")
    private LocalDate dateOfBirth;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Valid @NotNull
    private Address address;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "patient")
    private List<Appointment> appointments;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private User user;
}
