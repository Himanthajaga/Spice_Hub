package lk.ijse.back_end.service;

import lk.ijse.back_end.dto.BidDTO;

import java.util.List;
import java.util.UUID;

public interface BidService {
    void save(BidDTO bidDTO);
    List<BidDTO> getAll();
    void delete(UUID id);
    void update(BidDTO bidDTO);
}
