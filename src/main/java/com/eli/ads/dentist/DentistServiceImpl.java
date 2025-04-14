package com.eli.ads.dentist;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class DentistServiceImpl implements DentistService{

    private final DentistRepository dentistRepository; ;

    @Override
    public Dentist createDentist(Dentist dentist) {
        return dentistRepository.save(dentist);
    }
}
