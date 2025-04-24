package com.eli.ads.common.address;

import com.eli.ads.user.patient.PatientResponse;

public class AddressMapper {
    public AddressResponse toAddressResponse(Address address) {
        if (address == null) return null;
        return new AddressResponse(
                address.getId(),
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getZipCode(),
                new PatientResponse(
                        address.getPatient().getId(),
                        address.getPatient().getFirstName(),
                        address.getPatient().getPatientNumber(),
                        address.getPatient().getLastName(),
                        address.getPatient().getPhoneNumber(),
                        address.getPatient().getEmail(),
                        address.getPatient().getDateOfBirth(),
                        null
                )
        );
    }

}
