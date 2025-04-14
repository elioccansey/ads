package com.eli.ads.patient;

import com.eli.ads.appointment.Appointment;
import com.eli.ads.common.Address;
import com.eli.ads.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
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

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Address address;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;
}
