package com.eli.ads.common.address;

import com.eli.ads.patient.PatientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final PatientMapper patientMapper;
    private final AddressMapper addressMapper;

    public List<AddressResponse> getAllAddressesSortedByCity() {
        List<Address> addresses = addressRepository.getAllAddressesSortedByCity();
        return addresses.stream()
                .map(addressMapper::toAddressResponse)
                .collect(Collectors.toList());
    }
}
