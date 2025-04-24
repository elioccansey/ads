package com.eli.ads.appointment.surgery;

import com.eli.ads.appointment.surgery.Surgery;
import com.eli.ads.appointment.surgery.SurgeryRequest;
import com.eli.ads.appointment.surgery.SurgeryResponse;
import com.eli.ads.user.User;

import java.util.List;


public interface SurgeryService {
    Surgery createSurgery(Surgery surgery);
    SurgeryResponse createSurgery(SurgeryRequest request, User user);
    List<SurgeryResponse> getAllSurgeries(User user);
    SurgeryResponse getSurgeryById(Long id, User user);
}
