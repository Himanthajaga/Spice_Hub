package lk.ijse.back_end.service.impl;

import lk.ijse.back_end.dto.BidDTO;
import lk.ijse.back_end.repository.BidRepo;
import lk.ijse.back_end.service.BidService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BidServiceImpl implements BidService {
    @Autowired
    private BidRepo bidRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public void save(BidDTO bidDTO) {

    }

    @Override
    public List<BidDTO> getAll() {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public void update(BidDTO bidDTO) {

    }
}
