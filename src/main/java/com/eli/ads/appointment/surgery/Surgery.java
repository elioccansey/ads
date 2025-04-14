package com.eli.ads.appointment.surgery;

import com.eli.ads.common.Address;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "surgeries")
public class Surgery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Address address;
    private String phoneNumber;
}
