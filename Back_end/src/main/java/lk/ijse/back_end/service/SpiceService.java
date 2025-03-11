package lk.ijse.back_end.service;

import lk.ijse.back_end.dto.SpiceDTO;

import java.util.List;

public interface SpiceService {
    void save(SpiceDTO spiceDTO);
    List<SpiceDTO> getAll();
    void delete(Long id);
    void update(SpiceDTO spiceDTO);
}
