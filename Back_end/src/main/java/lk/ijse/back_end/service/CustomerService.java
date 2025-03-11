package lk.ijse.back_end.service;

import lk.ijse.back_end.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {
    void save(CustomerDTO customerDTO);
    List<CustomerDTO> getAll();
    void delete(int id);
    void update(CustomerDTO customerDTO);
}
