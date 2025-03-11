package lk.ijse.back_end.service.impl;

import lk.ijse.back_end.dto.PaymentDTO;
import lk.ijse.back_end.repository.PaymentRepo;
import lk.ijse.back_end.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepo paymentRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public void save(PaymentDTO paymentDTO) {

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
}
