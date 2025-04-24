package com.eli.ads.user.dentist;

import com.eli.ads.user.dentist.dto.DentistRequestDto;
import com.eli.ads.user.dentist.dto.DentistResponseDto;
import org.springframework.stereotype.Component;

@Component
public class DentistMapper {

    // Convert DentistRequestDto to Dentist entity
    public Dentist toDentist(DentistRequestDto dentistRequestDto) {
        Dentist dentist = new Dentist();
        dentist.setFirstName(dentistRequestDto.firstName());
        dentist.setLastName(dentistRequestDto.lastName());
        dentist.setPhoneNumber(dentistRequestDto.phoneNumber());
        dentist.setEmail(dentistRequestDto.email());
        dentist.setSpecialization(dentistRequestDto.specialization());
        return dentist;
    }

    // Convert Dentist entity to DentistResponseDto
    public DentistResponseDto toDentistResponseDto(Dentist dentist) {
        return new DentistResponseDto(
                dentist.getId(),
                dentist.getFirstName(),
                dentist.getLastName(),
                dentist.getPhoneNumber(),
                dentist.getEmail(),
                dentist.getSpecialization()
        );
    }

    // Update Dentist entity from DentistRequestDto
    public void updateDentistFromRequest(DentistRequestDto dentistRequestDto, Dentist dentist) {
        dentist.setFirstName(dentistRequestDto.firstName());
        dentist.setLastName(dentistRequestDto.lastName());
        dentist.setPhoneNumber(dentistRequestDto.phoneNumber());
        dentist.setEmail(dentistRequestDto.email());
        dentist.setSpecialization(dentistRequestDto.specialization());
    }
}
