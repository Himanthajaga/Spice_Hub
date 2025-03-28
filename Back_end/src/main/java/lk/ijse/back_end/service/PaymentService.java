package lk.ijse.back_end.service;

import lk.ijse.back_end.dto.PaymentDTO;

import java.util.List;

public interface PaymentService {
    void save(PaymentDTO paymentDTO);
    List<PaymentDTO> getAll();
    void delete(String id);
    void update(PaymentDTO paymentDTO);

    List<PaymentDTO> getAllPayments();
}
