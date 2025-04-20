package com.eli.ads.bootstrap;

import com.eli.ads.appointment.Appointment;
import com.eli.ads.appointment.AppointmentService;
import com.eli.ads.appointment.surgery.Surgery;
import com.eli.ads.appointment.surgery.SurgeryService;
import com.eli.ads.common.address.Address;
import com.eli.ads.dentist.Dentist;
import com.eli.ads.dentist.DentistService;
import com.eli.ads.patient.Patient;
import com.eli.ads.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final DentistService dentistService;
    private final PatientService patientService;
    private final SurgeryService surgeryService;
    private final AppointmentService appointmentService;

    @Override
    public void run(String... args) {
        // Create surgeries
        Surgery s10 = surgeryService.createSurgery(createSurgery("S10", "123 Elm St", "Springfield", "OH", "45501", "111-111-1110"));
        Surgery s13 = surgeryService.createSurgery(createSurgery("S13", "456 Oak St", "Springfield", "OH", "45502", "111-111-1113"));
        Surgery s15 = surgeryService.createSurgery(createSurgery("S15", "789 Pine St", "Springfield", "OH", "45503", "111-111-1115"));

        // Create dentists
        Dentist tony = dentistService.createDentist(createDentist("Tony", "Smith", "tony.smith@email.com", "Orthodontist", "555-1001"));
        Dentist helen = dentistService.createDentist(createDentist("Helen", "Pearson", "helen.pearson@email.com", "Pediatric", "555-1002"));
        Dentist robin = dentistService.createDentist(createDentist("Robin", "Plevin", "robin.plevin@email.com", "Endodontist", "555-1003"));

        // Create patients with addresses and set bidirectional relationship
        Patient gillian = patientService.createPatient(createPatient("P100", "Gillian", "White", "gillian.white@email.com", "555-2001", LocalDate.of(1990, 3, 15)));
        Patient jill = patientService.createPatient(createPatient("P105", "Jill", "Bell", "jill.bell@email.com", "555-2002", LocalDate.of(1985, 6, 20)));
        Patient ian = patientService.createPatient(createPatient("P108", "Ian", "MacKay", "ian.mackay@email.com", "555-2003", LocalDate.of(1982, 1, 10)));
        Patient john = patientService.createPatient(createPatient("P110", "John", "Walker", "john.walker@email.com", "555-2004", LocalDate.of(1978, 8, 25)));

        // Create appointments
        appointmentService.createAppointment(createAppointment(LocalDate.of(2013, 9, 12), LocalTime.of(10, 0), gillian, tony, s15));
        appointmentService.createAppointment(createAppointment(LocalDate.of(2013, 9, 12), LocalTime.of(12, 0), jill, tony, s15));
        appointmentService.createAppointment(createAppointment(LocalDate.of(2013, 9, 12), LocalTime.of(10, 0), ian, helen, s10));
        appointmentService.createAppointment(createAppointment(LocalDate.of(2013, 9, 14), LocalTime.of(14, 0), ian, helen, s10));
        appointmentService.createAppointment(createAppointment(LocalDate.of(2013, 9, 14), LocalTime.of(16, 30), jill, robin, s15));
        appointmentService.createAppointment(createAppointment(LocalDate.of(2013, 9, 15), LocalTime.of(18, 0), john, robin, s13));
    }

    private Dentist createDentist(String first, String last, String email, String specialization, String phone) {
        Dentist d = new Dentist();
        d.setFirstName(first);
        d.setLastName(last);
        d.setEmail(email);
        d.setSpecialization(specialization);
        d.setPhoneNumber(phone);
        return d;
    }

    private Patient createPatient(String patientNumber, String first, String last, String email, String phone, LocalDate dob) {
        Patient p = new Patient();
        p.setPatientNumber(patientNumber);
        p.setFirstName(first);
        p.setLastName(last);
        p.setEmail(email);
        p.setPhoneNumber(phone);
        p.setDateOfBirth(dob);

        // Create an address
        Address address = createAddress("123 Main St", "Springfield", "OH", "45505");

        // Set bidirectional relationship
        address.setPatient(p);  // Set the address's patient (back reference)
        p.setAddress(address);   // Set the patient's address

        return p;
    }

    private Surgery createSurgery(String name, String street, String city, String state, String zip, String phone) {
        Surgery s = new Surgery();
        s.setName(name);
        s.setPhoneNumber(phone);
        s.setAddress(createAddress(street, city, state, zip));
        return s;
    }

    private Address createAddress(String street, String city, String state, String zip) {
        Address a = new Address();
        a.setStreet(street);
        a.setCity(city);
        a.setState(state);
        a.setZipCode(zip);
        return a;
    }

    private Appointment createAppointment(LocalDate date, LocalTime time, Patient p, Dentist d, Surgery s) {
        Appointment a = new Appointment();
        a.setAppointmentDate(date);
        a.setAppointmentTime(time);
        a.setPatient(p);
        a.setDentist(d);
        a.setSurgery(s);
        return a;
    }
}
