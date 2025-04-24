package com.eli.ads.billing;

import com.eli.ads.user.User;

import java.util.List;

public interface BillService {
    List<BillResponse> getBillsByPatient(Long patientId, User user);
    BillResponse markBillAsPaid(Long billId, User user);
}
