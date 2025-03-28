package lk.ijse.back_end.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.back_end.dto.PaymentDTO;
import lk.ijse.back_end.entity.Payment;
import lk.ijse.back_end.repository.PaymentRepo;
import lk.ijse.back_end.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepo paymentRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public void save(PaymentDTO paymentDTO) {
        Payment payment = modelMapper.map(paymentDTO, Payment.class);
        paymentRepo.save(payment);
    }

    @Override
    public List<PaymentDTO> getAll() {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public void update(PaymentDTO paymentDTO) {

    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        List<Payment> payments = paymentRepo.findAll();
        return payments.stream().map(payment -> {
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setPid(payment.getPid());
            paymentDTO.setAmount(payment.getAmount());
            paymentDTO.setPaymentDate(payment.getPaymentdate());
            paymentDTO.setPaymentMethodId(payment.getPaymentMethodId());
            paymentDTO.setBidId(payment.getBidId());
            paymentDTO.setSpiceOwnerEmail(payment.getSpiceOwnerEmail());
            paymentDTO.setBuyerEmail(payment.getBuyerEmail());
            return paymentDTO;
        }).collect(Collectors.toList());
    }
}
