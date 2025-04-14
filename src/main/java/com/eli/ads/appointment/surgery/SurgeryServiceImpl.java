package com.eli.ads.appointment.surgery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SurgeryServiceImpl implements SurgeryService{

    private final SurgeryRepository surgeryRepository;
    @Override
    public Surgery createSurgery(Surgery surgery) {
        return surgeryRepository.save(surgery);
    }
}
