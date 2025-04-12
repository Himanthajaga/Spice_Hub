package lk.ijse.back_end.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.back_end.dto.PaymentDTO;
import lk.ijse.back_end.entity.Payment;
import lk.ijse.back_end.repository.PaymentRepo;
import lk.ijse.back_end.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    private static final Logger log = LoggerFactory.getLogger(SpiceServiceImpl.class);
    @Autowired
    private PaymentRepo paymentRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    public PaymentServiceImpl(PaymentRepo paymentRepo, ModelMapper modelMapper) {
        this.paymentRepo = paymentRepo;
        this.modelMapper = modelMapper;
        log.info("PaymentServiceImpl initialized");
    }

    @Override
    @Transactional
    public void save(PaymentDTO paymentDTO) {
        if (paymentDTO.getSpiceOwnerEmail() == null || paymentDTO.getSpiceOwnerEmail().isEmpty()) {
            throw new IllegalArgumentException("Spice Owner Email is missing in the PaymentDTO");
        }
        log.info("Saving payment: {}", paymentDTO); // Add this log
        Payment payment = modelMapper.map(paymentDTO, Payment.class);
        paymentRepo.save(payment);
        log.info("Payment saved successfully with ID: {}", payment.getPid()); // Add this log
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
            paymentDTO.setFriendlyId("PAY-" + payment.getPid().toString().substring(0, 8).toUpperCase());
            return paymentDTO;
        }).collect(Collectors.toList());
    }
}
