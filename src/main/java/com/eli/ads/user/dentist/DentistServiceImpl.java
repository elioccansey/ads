package com.eli.ads.user.dentist;

import com.eli.ads.appointment.Appointment;
import com.eli.ads.appointment.AppointmentService;
import com.eli.ads.user.User;
import com.eli.ads.user.UserService;
import com.eli.ads.user.auth.RegistrationRequest;
import com.eli.ads.user.dentist.dto.DentistRequestDto;
import com.eli.ads.user.dentist.dto.DentistResponseDto;
import com.eli.ads.user.permission.PermissionEnum;
import com.eli.ads.user.role.RoleEnum;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DentistServiceImpl implements DentistService {

    private final DentistRepository dentistRepository;
    private final UserService userService;
    private final AppointmentService appointmentService;
    private final DentistMapper dentistMapper;  // Use the mapper to map between Dentist and DentistResponseDto

    @Override
    public List<DentistResponseDto> getAllDentists(User user) throws AccessDeniedException {
        user.checkPermission(PermissionEnum.LIST_DENTISTS);

        return dentistRepository.findAll()
                .stream()
                .map(dentistMapper::toDentistResponseDto) // Use the mapper to convert Dentist to DentistResponseDto
                .collect(Collectors.toList());
    }

    @Override
    public DentistResponseDto getDentistById(Long id, User user) throws AccessDeniedException {
        user.checkPermission(PermissionEnum.VIEW_DENTIST);

        return dentistRepository.findById(id)
                .map(dentistMapper::toDentistResponseDto) // Use the mapper here too
                .orElse(null);
    }

    @Override
    public DentistResponseDto registerDentist(DentistRequestDto dentistRequest, User connectedUser) throws AccessDeniedException {
        connectedUser.checkPermission(PermissionEnum.REGISTER_DENTIST);
        Dentist dentist = dentistMapper.toDentist(dentistRequest);
        Long userId = dentistRequest.userId();
        if(userId != null){
            User foundUser = userService.findUserById(userId);
            dentist.setUser(foundUser);
        }else{
            dentist.setUser(userService.saveUser(
                    new RegistrationRequest(
                            dentistRequest.email(),
                            //TODO Generate Secure password
                            "defaultPassword",
                            Set.of(RoleEnum.PATIENT.name())
                    )
            ));
        }
        return dentistMapper.toDentistResponseDto(dentistRepository.save(dentist));
    }

    @Override
    public DentistResponseDto updateDentist(Long id, DentistRequestDto dentistRequestDto, User user) throws AccessDeniedException {
        user.checkPermission(PermissionEnum.UPDATE_DENTIST);

        Dentist dentist = dentistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dentist not found"));

        dentistMapper.updateDentistFromRequest(dentistRequestDto, dentist); // Update the entity using the mapper
        Dentist updatedDentist = dentistRepository.save(dentist);

        return dentistMapper.toDentistResponseDto(updatedDentist);
    }

    @Override
    @Transactional
    public void deleteDentist(Long dentistId,  User user) {
        user.checkPermission(PermissionEnum.DELETE_DENTIST);
        Dentist dentist = dentistRepository.findById(dentistId)
                .orElseThrow(() -> new EntityNotFoundException("Dentist not found"));
        appointmentService.removeDentistFromAppointments(dentistId);
        dentistRepository.delete(dentist);
    }

}
