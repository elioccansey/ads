package com.eli.ads.user.dentist;

import com.eli.ads.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Entity
@Data
@Table(name = "dentists")
public class Dentist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "First name is required.")
    private String firstName;
    @NotBlank(message = "Last name is required.")
    private String lastName;
    @NotBlank(message = "Phone number name is required.")
    private String phoneNumber;
    @NotBlank(message = "Email name is required.")
    @Email(message = "Enter a valid email.")
    private String email;
    @NotBlank(message = "Specialization is required.")
    private String specialization;
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
}