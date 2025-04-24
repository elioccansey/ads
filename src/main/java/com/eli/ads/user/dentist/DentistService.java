package com.eli.ads.user.dentist;

import com.eli.ads.user.dentist.dto.DentistRequestDto;
import com.eli.ads.user.dentist.dto.DentistResponseDto;
import com.eli.ads.user.User;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface DentistService {

    List<DentistResponseDto> getAllDentists(User user) throws AccessDeniedException;

    DentistResponseDto getDentistById(Long id, User user) throws AccessDeniedException;

    DentistResponseDto registerDentist(DentistRequestDto dentistRequestDto, User user) throws AccessDeniedException;

    DentistResponseDto updateDentist(Long id, DentistRequestDto dentistRequestDto, User user) throws AccessDeniedException;

    void deleteDentist(Long id, User user) throws AccessDeniedException;
}
