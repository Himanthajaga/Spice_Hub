package lk.ijse.back_end.service.impl;

import lk.ijse.back_end.dto.CustomerDTO;
import lk.ijse.back_end.repository.CustomerRepo;
import lk.ijse.back_end.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public void save(CustomerDTO customerDTO) {
}
    @Override
    public List<CustomerDTO> getAll() {
        return null;
    }
    @Override
    public void delete(int id) {
    }
    @Override
    public void update(CustomerDTO customerDTO) {
    }
}
