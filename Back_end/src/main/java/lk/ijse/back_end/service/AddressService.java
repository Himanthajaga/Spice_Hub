package lk.ijse.back_end.service;

import lk.ijse.back_end.dto.AddressDTO;

import java.util.List;

public interface AddressService {
    void save(AddressDTO addressDTO);
    List<AddressDTO> getAll();
    void delete(int id);
    void update(AddressDTO addressDTO);
}
