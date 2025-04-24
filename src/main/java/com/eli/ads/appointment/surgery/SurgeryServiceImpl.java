package com.eli.ads.appointment.surgery;

import com.eli.ads.user.User;
import com.eli.ads.user.permission.PermissionEnum;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurgeryServiceImpl implements SurgeryService {

    private final SurgeryRepository surgeryRepository;

    @Override
    public Surgery createSurgery(Surgery surgery) {
        return surgeryRepository.save(surgery);
    }

    @Override
    public SurgeryResponse createSurgery(SurgeryRequest request, User user) {
        user.checkPermission(PermissionEnum.CREATE_SURGERY);

        Surgery surgery = new Surgery();
        surgery.setName(request.name());
        surgery.setAddress(request.address());
        surgery.setPhoneNumber(request.phoneNumber());

        Surgery saved = surgeryRepository.save(surgery);
        return toResponse(saved);
    }

    @Override
    public List<SurgeryResponse> getAllSurgeries(User user) {
        user.checkPermission(PermissionEnum.LIST_SURGERIES);

        return surgeryRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SurgeryResponse getSurgeryById(Long id, User user) {
        user.checkPermission(PermissionEnum.VIEW_SURGERY);

        Surgery surgery = surgeryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Surgery not found"));

        return toResponse(surgery);
    }

    private SurgeryResponse toResponse(Surgery surgery) {
        return new SurgeryResponse(
                surgery.getId(),
                surgery.getName(),
                surgery.getAddress(),
                surgery.getPhoneNumber()
        );
    }
}
