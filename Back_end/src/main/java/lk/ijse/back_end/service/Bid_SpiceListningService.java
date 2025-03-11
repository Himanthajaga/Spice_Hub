package lk.ijse.back_end.service;

import lk.ijse.back_end.dto.BidSpiceListningDTO;

import java.util.List;

public interface Bid_SpiceListningService {
    void save (BidSpiceListningDTO bidSpiceListningDTO);
    List<BidSpiceListningDTO> getAll();
    void delete(String id);
    void update(BidSpiceListningDTO bidSpiceListningDTO);
}
