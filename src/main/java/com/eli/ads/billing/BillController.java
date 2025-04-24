package com.eli.ads.billing;

import com.eli.ads.user.User;
import com.eli.ads.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bills")
public class BillController {

    private final BillService billService;
    private final UserService userService;

    @PreAuthorize("hasAuthority('VIEW_BILL')")
    @GetMapping("/patient/{patient-id}")
    @ResponseStatus(HttpStatus.OK)
    public List<BillResponse> getBillsByPatient(@PathVariable("patient-id") Long patientId) throws AccessDeniedException {
        User user = userService.getConnectedUser();
        return billService.getBillsByPatient(patientId, user);
    }

    @PreAuthorize("hasAuthority('UPDATE_BILL')")
    @PutMapping("/{bill-id}")
    @ResponseStatus(HttpStatus.OK)
    public BillResponse markBillAsPaid(@PathVariable("bill-id") Long billId) throws AccessDeniedException {
        User user = userService.getConnectedUser();
        return billService.markBillAsPaid(billId, user);
    }
}
