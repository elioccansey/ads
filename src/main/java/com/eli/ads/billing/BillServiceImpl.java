package com.eli.ads.billing;

import com.eli.ads.user.User;
import com.eli.ads.user.permission.PermissionEnum;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;

    @Override
    public List<BillResponse> getBillsByPatient(Long patientId, User user) {
        user.checkPermission(PermissionEnum.VIEW_BILL);

        return billRepository.findByPatientId(patientId).stream()
                .map(bill -> new BillResponse(
                        bill.getId(),
                        bill.getAmount().doubleValue(),
                        bill.isPaid()
                )).collect(Collectors.toList());
    }

    @Override
    public BillResponse markBillAsPaid(Long billId, User user) {
        user.checkPermission(PermissionEnum.UPDATE_BILL);

        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new EntityNotFoundException("Bill not found"));

        bill.setPaid(true);
        Bill updated = billRepository.save(bill);

        return new BillResponse(updated.getId(), updated.getAmount().doubleValue(), updated.isPaid());
    }
}