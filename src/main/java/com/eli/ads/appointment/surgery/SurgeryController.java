package com.eli.ads.appointment.surgery;

import com.eli.ads.user.User;
import com.eli.ads.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/surgeries")
@RequiredArgsConstructor
public class SurgeryController {

    private final SurgeryService surgeryService;
    private final UserService userService;

    // Create a new surgery - requires CREATE_SURGERY permission
    @PreAuthorize("hasAuthority('CREATE_SURGERY')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SurgeryResponse createSurgery(@RequestBody SurgeryRequest request) throws AccessDeniedException {
        User user = userService.getConnectedUser();
        return surgeryService.createSurgery(request, user);
    }

    // Get all surgeries - requires VIEW_SURGERY permission
    @PreAuthorize("hasAuthority('VIEW_SURGERY')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SurgeryResponse> getAllSurgeries() throws AccessDeniedException {
        User user = userService.getConnectedUser();
        return surgeryService.getAllSurgeries(user);
    }

    // Get a specific surgery - requires VIEW_SURGERY permission
    @PreAuthorize("hasAuthority('VIEW_SURGERY')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SurgeryResponse getSurgeryById(@PathVariable Long id) throws AccessDeniedException {
        User user = userService.getConnectedUser();
        return surgeryService.getSurgeryById(id, user);
    }
}
