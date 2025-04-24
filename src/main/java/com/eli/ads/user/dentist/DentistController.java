package com.eli.ads.user.dentist;

import com.eli.ads.user.UserService;
import com.eli.ads.user.dentist.dto.DentistRequestDto;
import com.eli.ads.user.dentist.dto.DentistResponseDto;
import com.eli.ads.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/dentists")
@RequiredArgsConstructor
public class DentistController {

    private final DentistService dentistService;
    private final UserService userService;

    @PreAuthorize("hasAuthority('LIST_DENTISTS')")
    @GetMapping
    public ResponseEntity<List<DentistResponseDto>> getAllDentists() throws AccessDeniedException {
        User user = userService.getConnectedUser();
        List<DentistResponseDto> dentists = dentistService.getAllDentists(user);
        return ResponseEntity.ok(dentists);
    }

    @PreAuthorize("hasAuthority('REGISTER_DENTIST')")
    @PostMapping
    public ResponseEntity<DentistResponseDto> registerDentist(@RequestBody DentistRequestDto dentistRequestDto) throws AccessDeniedException {
        User user = userService.getConnectedUser();
        DentistResponseDto responseDto = dentistService.registerDentist(dentistRequestDto, user);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('VIEW_DENTIST')")
    @GetMapping("/{id}")
    public ResponseEntity<DentistResponseDto> getDentist(@PathVariable Long id) throws AccessDeniedException {
        User user = userService.getConnectedUser();
        DentistResponseDto responseDto = dentistService.getDentistById(id, user);

        if (responseDto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(responseDto);
    }

    @PreAuthorize("hasAuthority('UPDATE_DENTIST')")
    @PutMapping("/{id}")
    public ResponseEntity<DentistResponseDto> updateDentist(@PathVariable Long id, @RequestBody DentistRequestDto dentistRequestDto) throws AccessDeniedException {
        User user = userService.getConnectedUser();
        DentistResponseDto updatedDentist = dentistService.updateDentist(id, dentistRequestDto, user);
        return ResponseEntity.ok(updatedDentist);
    }

    @PreAuthorize("hasAuthority('DELETE_DENTIST')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDentist(@PathVariable Long id) throws AccessDeniedException {
        User user = userService.getConnectedUser();
        dentistService.deleteDentist(id, user);
        return ResponseEntity.noContent().build();
    }
}
