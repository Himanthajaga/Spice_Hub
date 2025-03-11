package lk.ijse.back_end.service.impl;

import lk.ijse.back_end.dto.BidSpiceListningDTO;
import lk.ijse.back_end.repository.Bid_SpiceListingRepo;
import lk.ijse.back_end.service.Bid_SpiceListningService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Bid_SpiceListningServiceImpl implements Bid_SpiceListningService {
    @Autowired
    private Bid_SpiceListingRepo bid_spiceListningRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public void save(BidSpiceListningDTO bidSpiceListningDTO) {

    }

    @Override
    public List<BidSpiceListningDTO> getAll() {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public void update(BidSpiceListningDTO bidSpiceListningDTO) {

    }
}
