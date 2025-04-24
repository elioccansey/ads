package com.eli.ads.common.bootstrap;

import com.eli.ads.appointment.Appointment;
import com.eli.ads.appointment.AppointmentRepository;
import com.eli.ads.appointment.surgery.Surgery;
import com.eli.ads.appointment.surgery.SurgeryRepository;
import com.eli.ads.billing.Bill;
import com.eli.ads.billing.BillRepository;
import com.eli.ads.common.address.Address;
import com.eli.ads.user.User;
import com.eli.ads.user.UserRepository;
import com.eli.ads.user.dentist.Dentist;
import com.eli.ads.user.dentist.DentistRepository;
import com.eli.ads.user.patient.Patient;
import com.eli.ads.user.patient.PatientRepository;
import com.eli.ads.user.permission.Permission;
import com.eli.ads.user.permission.PermissionEnum;
import com.eli.ads.user.permission.PermissionRepository;
import com.eli.ads.user.role.Role;
import com.eli.ads.user.role.RoleEnum;
import com.eli.ads.user.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final DentistRepository dentistRepository;
    private final PatientRepository patientRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppointmentRepository appointmentRepository;
    private final SurgeryRepository surgeryRepository;
    private final BillRepository billRepository;

    @Override
    public void run(String... args) {
        for (PermissionEnum perm : PermissionEnum.values()) {
            permissionRepository.findByName(perm)
                    .orElseGet(() -> permissionRepository.save(new Permission(null, perm)));
        }

        for (RoleEnum roleEnum : RoleEnum.values()) {
            Set<Permission> permissions = roleEnum.getPermissions().stream()
                    .map(p -> permissionRepository.findByName(p).orElseThrow())
                    .collect(Collectors.toSet());

            roleRepository.findByName(roleEnum).orElseGet(() -> {
                Role role = new Role();
                role.setName(roleEnum);
                role.setPermissions(permissions);
                return roleRepository.save(role);
            });
        }

        Address surgeryAddress = new Address();
        surgeryAddress.setStreet("123 Main St");
        surgeryAddress.setCity("Toothville");
        surgeryAddress.setState("TX");
        surgeryAddress.setZipCode("75001");

        Surgery surgery = new Surgery();
        surgery.setName("Central Dental");
        surgery.setPhoneNumber("123456789");
        surgery.setAddress(surgeryAddress);
        surgery = surgeryRepository.save(surgery);

        User dentistUser1 = new User();
        dentistUser1.setUsername("dentist1");
        dentistUser1.setPassword(passwordEncoder.encode("pass123"));
        dentistUser1.setRoles(Set.of(roleRepository.findByName(RoleEnum.DENTIST).orElseThrow()));
        dentistUser1 = userRepository.save(dentistUser1);

        Dentist busyDentist = new Dentist();
        busyDentist.setFirstName("Busy");
        busyDentist.setLastName("Dentist");
        busyDentist.setEmail("busy@clinic.com");
        busyDentist.setPhoneNumber("111111111");
        busyDentist.setSpecialization("Endodontics");
        busyDentist.setUser(dentistUser1);
        busyDentist = dentistRepository.save(busyDentist);

        User patientUser1 = new User();
        patientUser1.setUsername("patient1");
        patientUser1.setPassword(passwordEncoder.encode("pass123"));
        patientUser1.setRoles(Set.of(roleRepository.findByName(RoleEnum.PATIENT).orElseThrow()));
        patientUser1 = userRepository.save(patientUser1);

        Address patientAddress1 = new Address();
        patientAddress1.setStreet("456 Bill St");
        patientAddress1.setCity("Debttown");
        patientAddress1.setState("TX");
        patientAddress1.setZipCode("75002");

        Patient unpaidPatient = new Patient();
        unpaidPatient.setFirstName("Unpaid");
        unpaidPatient.setLastName("Bill");
        unpaidPatient.setEmail("unpaid@patient.com");
        unpaidPatient.setPhoneNumber("222222222");
        unpaidPatient.setPatientNumber("P0001");
        unpaidPatient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        unpaidPatient.setAddress(patientAddress1);
        unpaidPatient.setUser(patientUser1);
        unpaidPatient = patientRepository.save(unpaidPatient);

        Bill unpaidBill = new Bill();
        unpaidBill.setPatient(unpaidPatient);
        unpaidBill.setAmount(BigDecimal.valueOf(150.00));
        unpaidBill.setPaid(false);
        billRepository.save(unpaidBill);

        LocalDate weekStart = LocalDate.of(2025, 4, 28);
        final Dentist finalBusyDentist = busyDentist;
        final Patient finalUnpaidPatient = unpaidPatient;
        final Surgery finalSurgery = surgery;

        for (int i = 0; i < 5; i++) {
            Appointment a = new Appointment();
            a.setAppointmentDate(weekStart.plusDays(i));
            a.setAppointmentTime(LocalTime.of(9 + i, 0));
            a.setDentist(finalBusyDentist);
            a.setPatient(finalUnpaidPatient);
            a.setSurgery(finalSurgery);
            appointmentRepository.save(a);
        }

        User dentistUser2 = new User();
        dentistUser2.setUsername("dentist2");
        dentistUser2.setPassword(passwordEncoder.encode("pass123"));
        dentistUser2.setRoles(Set.of(roleRepository.findByName(RoleEnum.DENTIST).orElseThrow()));
        dentistUser2 = userRepository.save(dentistUser2);

        Dentist freeDentist = new Dentist();
        freeDentist.setFirstName("Free");
        freeDentist.setLastName("Dentist");
        freeDentist.setEmail("free@clinic.com");
        freeDentist.setPhoneNumber("333333333");
        freeDentist.setSpecialization("General");
        freeDentist.setUser(dentistUser2);
        dentistRepository.save(freeDentist);

        User patientUser2 = new User();
        patientUser2.setUsername("patient2");
        patientUser2.setPassword(passwordEncoder.encode("pass123"));
        patientUser2.setRoles(Set.of(roleRepository.findByName(RoleEnum.PATIENT).orElseThrow()));
        patientUser2 = userRepository.save(patientUser2);

        Address patientAddress2 = new Address();
        patientAddress2.setStreet("789 Fresh Rd");
        patientAddress2.setCity("CleanCity");
        patientAddress2.setState("TX");
        patientAddress2.setZipCode("75003");

        Patient cleanPatient = new Patient();
        cleanPatient.setFirstName("Clean");
        cleanPatient.setLastName("Bill");
        cleanPatient.setEmail("clean@patient.com");
        cleanPatient.setPhoneNumber("444444444");
        cleanPatient.setPatientNumber("P0002");
        cleanPatient.setDateOfBirth(LocalDate.of(1995, 5, 15));
        cleanPatient.setAddress(patientAddress2);
        cleanPatient.setUser(patientUser2);
        patientRepository.save(cleanPatient);
    }
}