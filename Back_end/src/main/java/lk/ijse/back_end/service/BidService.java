package lk.ijse.back_end.service;

import lk.ijse.back_end.dto.BidDTO;

import java.util.List;

public interface BidService {
    void save(BidDTO bidDTO);
    List<BidDTO> getAll();
    void delete(Long id);
    void update(BidDTO bidDTO);
}
